package lang.app.llearning.ui.screens.generateStory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lang.app.llearning.data.model.Story
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
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val ttsUiState = textToSpeechViewModel.ttsUiState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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

            // English story sentences
            englishStorySplitted.forEachIndexed { index, sentence ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                            .combinedClickable(
                                onClick = {
                                    highlightedIndex = index
                                },
                                onLongClick = {
                                    currentlyFetchingSentenceIndex = index
                                    coroutineScope.launch {
                                        textToSpeechViewModel.fetchAndPlayAudio(sentence, context)
                                    }
                                }
                            )
                            .background(
                                if (highlightedIndex == index) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .align(Alignment.CenterStart)
                    )

                    if (currentlyFetchingSentenceIndex == index && textToSpeechViewModel.ttsUiState is TextToSpeechUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 2.dp,
                modifier = modifier.padding(vertical = 1.dp)
            )

            // Translated story sentences
            translatedStorySplitted.forEachIndexed { index, sentence ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                            .combinedClickable(
                                onClick = {
                                    highlightedIndex = index
                                },
                                onLongClick = {
                                    currentlyFetchingSentenceIndex = index
                                    coroutineScope.launch {
                                        textToSpeechViewModel.fetchAndPlayAudio(sentence, context)
                                    }
                                }
                            )
                            .background(
                                if (highlightedIndex == index) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .align(Alignment.CenterStart)
                    )

                    if (currentlyFetchingSentenceIndex == index && textToSpeechViewModel.ttsUiState is TextToSpeechUiState.Loading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        )
    }
}