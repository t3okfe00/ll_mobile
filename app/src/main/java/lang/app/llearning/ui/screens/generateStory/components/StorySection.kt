package lang.app.llearning.ui.screens.generateStory.components
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lang.app.llearning.model.Story
import lang.app.llearning.ui.components.DisplayStory
import lang.app.llearning.ui.screens.generateStory.splitStoryIntoSentences
import lang.app.llearning.viewmodel.TextToSpeechUiState
import lang.app.llearning.viewmodel.TextToSpeechViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorySection(
    modifier: Modifier = Modifier,
    story: Story,
    textToSpeechViewModel: TextToSpeechViewModel
) {
    val (englishStory, translatedStory) = story
    val englishStorySplitted = splitStoryIntoSentences(englishStory)
    val translatedStorySplitted = splitStoryIntoSentences(translatedStory)
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    val ttsUiState = textToSpeechViewModel.ttsUiState
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    var currentlyFetchingSentenceIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)) {

            if (ttsUiState is TextToSpeechUiState.Error) {
                LaunchedEffect(key1 = ttsUiState) {
                    snackbarHostState.showSnackbar(ttsUiState.message)
                }
            }

            // Display English story
            DisplayStory(
                sentences = englishStorySplitted,
                highlightedIndex = highlightedIndex,
                currentlyFetchingSentenceIndex = currentlyFetchingSentenceIndex,
                onSentenceClick = { index -> highlightedIndex = index },
                onSentenceLongClick = { index ->
                    currentlyFetchingSentenceIndex = index
                    coroutineScope.launch {
                        textToSpeechViewModel.fetchAndPlayAudio(englishStorySplitted[index], context)
                    }
                },
                textToSpeechUiState = ttsUiState,

            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 2.dp,
                modifier = modifier.padding(vertical = 1.dp)
            )

            // Display Translated story
            DisplayStory(
                sentences = translatedStorySplitted,
                highlightedIndex = highlightedIndex,
                currentlyFetchingSentenceIndex = currentlyFetchingSentenceIndex,
                onSentenceClick = { index -> highlightedIndex = index },
                onSentenceLongClick = { index ->
                    currentlyFetchingSentenceIndex = index
                    coroutineScope.launch {
                        textToSpeechViewModel.fetchAndPlayAudio(translatedStorySplitted[index], context)
                    }
                },
                textToSpeechUiState = ttsUiState,

            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        )
    }
}