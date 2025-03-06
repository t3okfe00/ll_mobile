package lang.app.llearning.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.launch
import lang.app.llearning.data.model.TextToSpeechApi
import lang.app.llearning.data.model.TextToSpeechRequest
import lang.app.llearning.data.model.TextToSpeechResponse

sealed interface TextToSpeechUiState {
    data class Success(val audioContent: String) : TextToSpeechUiState
    data object Initial : TextToSpeechUiState
    data class Error(val message: String) : TextToSpeechUiState
    data object Loading : TextToSpeechUiState
}

class TextToSpeechViewModel : ViewModel() {
    private var player: ExoPlayer? = null
    var ttsUiState: TextToSpeechUiState by mutableStateOf(TextToSpeechUiState.Initial)

    fun initializePlayer(context: Context) {
        player = ExoPlayer.Builder(context).build()
    }

    fun fetchAndPlayAudio(text: String, context: Context) {
        viewModelScope.launch {
            ttsUiState = TextToSpeechUiState.Loading
            try {
                val ttsApi = TextToSpeechApi.getInstance()
                val request = TextToSpeechRequest(text)
                val response: TextToSpeechResponse = ttsApi.fetchAudio(request)

                player?.let { exoPlayer ->
                    exoPlayer.stop()
                    exoPlayer.clearMediaItems()
                }

                if (player == null) {
                    initializePlayer(context)
                }

                player?.apply {
                    setMediaItem(MediaItem.fromUri(response.url))
                    prepare()
                    play()
                }

                ttsUiState = TextToSpeechUiState.Success(response.url)
            } catch (e: Exception) {
                ttsUiState = TextToSpeechUiState.Error("Text-To-Speech functionality is is temporarily not available"?: "An error occurred")

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}