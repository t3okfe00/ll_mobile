package lang.app.llearning.ui.screens.generateStory

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import lang.app.llearning.ui.screens.LoadingScreen
import lang.app.llearning.viewmodel.StoryUiState

@Composable
fun StoryRenderer(modifier: Modifier = Modifier, uiState: StoryUiState) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (uiState is StoryUiState.Error) {
        LaunchedEffect(uiState) {
            scope.launch {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    when(uiState){
        is StoryUiState.Loading -> LoadingScreen(modifier)
        is StoryUiState.Success -> StorySection(modifier, story = uiState.story, textToSpeechViewModel = viewModel())

        is StoryUiState.Error -> {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
            )
        }
        else -> {
        }
    }
}
