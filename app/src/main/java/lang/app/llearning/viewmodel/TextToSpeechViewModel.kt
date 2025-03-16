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
import lang.app.llearning.model.TextToSpeechApi
import lang.app.llearning.model.TextToSpeechRequest
import lang.app.llearning.model.TextToSpeechResponse
import org.json.JSONObject

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
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No Internet Connection"
                    is retrofit2.HttpException -> {

                        val errorBody = e.response()?.errorBody()?.string()
                        Log.d("errorBody", errorBody.toString())
                        try {

                            val errorJson = errorBody?.let { JSONObject(it) }
                            errorJson?.optString("message")
                                ?: errorJson?.optString("error")
                                ?: "API error: ${e.code()}"
                        } catch (jsonException: Exception) {
                            // Fallback to the original error message
                            "API error: ${e.message}"
                        }
                    }

                    else -> "An unexpected error occurred,Please try again later"
                }
                ttsUiState = TextToSpeechUiState.Error(errorMessage ?: "An error occurred")

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player?.release()
        player = null
    }
}