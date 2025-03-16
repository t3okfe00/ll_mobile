package lang.app.llearning.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lang.app.llearning.viewmodel.TextToSpeechUiState


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayStory(
    sentences: List<String>,
    highlightedIndex: Int?,
    currentlyFetchingSentenceIndex: Int?,
    onSentenceClick: (Int) -> Unit,
    onSentenceLongClick: (Int) -> Unit,
    textToSpeechUiState: TextToSpeechUiState,

) {
    sentences.forEachIndexed { index, sentence ->
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = sentence,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onSentenceClick(index) },
                        onLongClick = { onSentenceLongClick(index) }
                    )
                    .background(
                        if (highlightedIndex == index) MaterialTheme.colorScheme.inversePrimary
                        else MaterialTheme.colorScheme.onSecondary
                    )
                    .align(Alignment.CenterStart)
            )

            if (currentlyFetchingSentenceIndex == index && textToSpeechUiState is TextToSpeechUiState.Loading) {
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