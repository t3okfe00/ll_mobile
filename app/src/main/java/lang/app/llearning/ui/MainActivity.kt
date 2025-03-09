package lang.app.llearning.ui
import lang.app.llearning.viewmodel.StoryViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import lang.app.llearning.ui.screens.generateStory.GenerateStoryScreen
import lang.app.llearning.ui.screens.HomeScreen
import lang.app.llearning.ui.screens.ProfileScreen
import lang.app.llearning.ui.screens.TopBar
import lang.app.llearning.ui.shared.BottomNavigationBar
import lang.app.llearning.ui.theme.AppTheme
import lang.app.llearning.viewmodel.AuthViewModel


class MainActivity : ComponentActivity() {
    private val storyViewModel: StoryViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(modifier = Modifier,storyViewModel = storyViewModel,authViewModel = authViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier:Modifier = Modifier,
               windowSizeClass : WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
               storyViewModel: StoryViewModel,
               authViewModel: AuthViewModel

) {
    val widthSizeClass = windowSizeClass.windowWidthSizeClass
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color(0xFF3498db)),
        topBar = { if(widthSizeClass == WindowWidthSizeClass.COMPACT) TopBar(modifier=Modifier,2300,11) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = "home") {
                HomeScreen(navController, modifier,authViewModel)
            }
            composable(route = "profile") {
                ProfileScreen(navController, modifier,{},{},authViewModel)
            }
            composable(route = "story"){
                GenerateStoryScreen(modifier, storyViewModel = storyViewModel)
            }
        }

    }
}

