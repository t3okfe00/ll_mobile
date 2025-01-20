package lang.app.llearning.ui

import StoryViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import lang.app.llearning.R

import lang.app.llearning.ui.screens.GenerateStoryScreen
import lang.app.llearning.ui.screens.HomeScreen
import lang.app.llearning.ui.screens.ProfileScreen
import lang.app.llearning.ui.screens.TopBar
import lang.app.llearning.ui.theme.AppTheme


class MainActivity : ComponentActivity() {
    private val storyViewModel: StoryViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(modifier = Modifier,storyViewModel = storyViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier:Modifier = Modifier,
               windowSizeClass : WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
               storyViewModel: StoryViewModel
) {
    val widthSizeClass = windowSizeClass.windowWidthSizeClass
    Log.v("WindowSizeClass", "Size Class: $widthSizeClass")
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        "home" to R.drawable.home,
        "profile" to R.drawable.profile,
        "story" to R.drawable.books

    )

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color(0xFF3498db)),
        topBar = { if(widthSizeClass == WindowWidthSizeClass.COMPACT) TopBar(modifier, tokens = 3000, storiesCreated = 23) },
        bottomBar = {
            NavigationBar {
                navigationItems.forEachIndexed { index, (label, iconResId) ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(iconResId),
                                contentDescription = label,
                                modifier = Modifier.size(35.dp)
                            )
                        },
                        label = { Text(label.uppercase()) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            navController.navigate(label)
                        }
                    )
                }
            }

        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = "home") {
                HomeScreen(navController, modifier)
            }
            composable(route = "profile") {
                ProfileScreen(navController, modifier,"Efe",{},{})
            }
            composable(route = "story"){
                GenerateStoryScreen(navController,modifier, storyViewModel = storyViewModel)
            }
        }

    }
}

