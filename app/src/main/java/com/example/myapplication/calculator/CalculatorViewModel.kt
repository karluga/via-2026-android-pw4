package com.example.myapplication.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode

data class CalculatorUiState(
    val display: String = "0",
    val expression: String = "",
    val memory: Double? = null,
    val previousOperand: Double? = null,
    val pendingOperation: String? = null,
    val isNewInput: Boolean = true,
    val isError: Boolean = false
)

class CalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalculatorUiState())
    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun onDigitClick(digit: String) {
        _uiState.update { state ->
            if (state.isError) {
                state.copy(
                    display = digit,
                    isNewInput = false,
                    isError = false
                )
            } else if (state.isNewInput || state.display == "0") {
                state.copy(
                    display = digit,
                    isNewInput = false
                )
            } else {
                state.copy(
                    display = state.display + digit
                )
            }
        }
    }

    fun onDecimalClick() {
        _uiState.update { state ->
            if (state.isError) {
                state.copy(
                    display = "0.",
                    isNewInput = false,
                    isError = false
                )
            } else if (state.isNewInput) {
                state.copy(
                    display = "0.",
                    isNewInput = false
                )
            } else if (!state.display.contains(".")) {
                state.copy(
                    display = state.display + "."
                )
            } else {
                state
            }
        }
    }

    fun onOperatorClick(op: String) {
        _uiState.update { state ->
            if (state.isError) return@update state

            val currentVal = state.display.toDoubleOrNull() ?: return@update state

            if (state.previousOperand != null && state.pendingOperation != null && !state.isNewInput) {
                val result = performCalculation(state.previousOperand, currentVal, state.pendingOperation)
                if (result == null) {
                    state.copy(
                        display = "Error",
                        expression = "${formatResult(state.previousOperand)} ${state.pendingOperation} ${formatResult(currentVal)} =",
                        isError = true,
                        previousOperand = null,
                        pendingOperation = null,
                        isNewInput = true
                    )
                } else {
                    val formattedResult = formatResult(result)
                    state.copy(
                        display = formattedResult,
                        expression = "$formattedResult $op",
                        previousOperand = result,
                        pendingOperation = op,
                        isNewInput = true
                    )
                }
            } else {
                val formattedVal = formatResult(currentVal)
                state.copy(
                    previousOperand = currentVal,
                    pendingOperation = op,
                    expression = "$formattedVal $op",
                    isNewInput = true
                )
            }
        }
    }

    fun onEqualsClick() {
        _uiState.update { state ->
            if (state.isError) return@update state
            val prev = state.previousOperand ?: return@update state
            val op = state.pendingOperation ?: return@update state
            val current = state.display.toDoubleOrNull() ?: return@update state

            val result = performCalculation(prev, current, op)
            if (result == null) {
                state.copy(
                    display = "Error",
                    expression = "${formatResult(prev)} $op ${formatResult(current)} =",
                    isError = true,
                    previousOperand = null,
                    pendingOperation = null,
                    isNewInput = true
                )
            } else {
                val formattedResult = formatResult(result)
                state.copy(
                    display = formattedResult,
                    expression = "${formatResult(prev)} $op ${formatResult(current)} =",
                    previousOperand = null,
                    pendingOperation = null,
                    isNewInput = true
                )
            }
        }
    }

    fun onClearClick() {
        _uiState.update { state ->
            state.copy(
                display = "0",
                expression = "",
                previousOperand = null,
                pendingOperation = null,
                isNewInput = true,
                isError = false
            )
        }
    }

    fun onClearEntryClick() {
        _uiState.update { state ->
            state.copy(
                display = "0",
                isNewInput = true,
                isError = false
            )
        }
    }

    fun onBackspaceClick() {
        _uiState.update { state ->
            if (state.isError || state.isNewInput) {
                state.copy(display = "0", isNewInput = true, isError = false)
            } else if (state.display.length > 1) {
                val newDisplay = state.display.dropLast(1)
                if (newDisplay == "-" || newDisplay.isEmpty()) {
                    state.copy(display = "0", isNewInput = true)
                } else {
                    state.copy(display = newDisplay)
                }
            } else {
                state.copy(display = "0", isNewInput = true)
            }
        }
    }

    fun onToggleSignClick() {
        _uiState.update { state ->
            if (state.isError || state.display == "0") return@update state
            val newDisplay = if (state.display.startsWith("-")) {
                state.display.removePrefix("-")
            } else {
                "-${state.display}"
            }
            state.copy(display = newDisplay)
        }
    }

    // --- Memory Operations ---

    /**
     * MS (Memory Save): Saves current displayed number into memory.
     */
    fun onMemorySave() {
        _uiState.update { state ->
            val currentVal = state.display.toDoubleOrNull()
            if (currentVal != null && !state.isError) {
                state.copy(
                    memory = currentVal,
                    isNewInput = true
                )
            } else {
                state
            }
        }
    }

    /**
     * MR (Memory Read): Reads from memory and replaces display so it can be used for further calculations.
     */
    fun onMemoryRead() {
        _uiState.update { state ->
            if (state.memory != null) {
                state.copy(
                    display = formatResult(state.memory),
                    isNewInput = true,
                    isError = false
                )
            } else {
                state
            }
        }
    }

    /**
     * MC (Memory Clear): Clears saved memory value.
     */
    fun onMemoryClear() {
        _uiState.update { state ->
            state.copy(memory = null)
        }
    }

    private fun performCalculation(num1: Double, num2: Double, op: String): Double? {
        return when (op) {
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> if (num2 == 0.0) null else num1 / num2
            else -> null
        }
    }

    companion object {
        fun formatResult(value: Double): String {
            if (value.isNaN() || value.isInfinite()) return "Error"
            return try {
                val bd = BigDecimal(value.toString())
                    .setScale(10, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                bd.toPlainString()
            } catch (_: Exception) {
                value.toString()
            }
        }
    }
}
