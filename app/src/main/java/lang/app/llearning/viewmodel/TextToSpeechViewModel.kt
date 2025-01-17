package lang.app.llearning.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lang.app.llearning.data.model.TextToSpeechApi
import lang.app.llearning.data.model.TextToSpeechRequest
import lang.app.llearning.data.model.TextToSpeechResponse

sealed interface TextToSpeechUiState {
    data class Success(val audioContent: String) : TextToSpeechUiState
    object Initial : TextToSpeechUiState
    object Error : TextToSpeechUiState
    object Loading : TextToSpeechUiState
}

class TextToSpeechViewModel : ViewModel() {
    var ttsUiState: TextToSpeechUiState by mutableStateOf(TextToSpeechUiState.Initial)

    fun synthesizeSpeech(text: String) {
        viewModelScope.launch {
            ttsUiState = TextToSpeechUiState.Loading
            try {
                val ttsApi = TextToSpeechApi.getInstance()
                val request = TextToSpeechRequest(text)
                val response: TextToSpeechResponse = ttsApi.synthesizeSpeech(request)
                ttsUiState = TextToSpeechUiState.Success(response.audioContent)
            } catch (e: Exception) {
                Log.d("ERROR", e.message.toString())
                ttsUiState = TextToSpeechUiState.Error
            }
        }
    }
}
