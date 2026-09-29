package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MyApplicationApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun MyApplicationApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    // Wrap the entire app hierarchy in a SelectionContainer to make all text globally selectable
    SelectionContainer {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                AppDestinations.entries.forEach {
                    item(
                        icon = {
                            Icon(
                                painter = painterResource(it.icon),
                                contentDescription = it.label,
                                tint = if (it == AppDestinations.HOME) Color.Unspecified else LocalContentColor.current
                            )
                        },
                        label = { Text(it.label) },
                        selected = it == currentDestination,
                        onClick = { currentDestination = it }
                    )
                }
            }
        ) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                // Dynamic page switching based on current state
                when (currentDestination) {
                    AppDestinations.HOME -> HomeScreen(modifier = Modifier.padding(innerPadding))
                    AppDestinations.FAVORITES -> FavoritesScreen(modifier = Modifier.padding(innerPadding))
                    AppDestinations.PROFILE -> ProfileScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

enum class AppDestinations(
    val label: String,
    val icon: Int,
) {
    HOME("Home", R.drawable.among_us_6008615_1920),
    FAVORITES("Favorites", R.drawable.ic_favorite),
    PROFILE("Profile", R.drawable.ic_account_box),
}


@Preview(showBackground = true)

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Text(text = "This is the first application from Group 2:\n" +
            "Kārlis Ivars Braķis\n" +
            "Andrejs Ņesterovičs\n" +
            "\tAnd the application was developed by\n" +
            "Kārlis Ivars Braķis",
        modifier = modifier,
        fontSize = 40.sp,       // Lowered slightly to fit well
        lineHeight = 40.sp)
}

@Composable
fun FavoritesScreen(modifier: Modifier = Modifier) {
    Text(text = "Your Favorite Items", modifier = modifier)
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Text(text = "User Profile Settings", modifier = modifier)
}