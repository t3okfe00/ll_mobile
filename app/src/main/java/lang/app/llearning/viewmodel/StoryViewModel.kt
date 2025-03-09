package lang.app.llearning.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lang.app.llearning.data.model.Story
import lang.app.llearning.data.model.StoryApi
import lang.app.llearning.data.model.StoryRequest
import org.json.JSONObject

sealed interface StoryUiState {
    data class Success(val story: Story): StoryUiState
    data object Initial: StoryUiState
    data class Error(val message:String): StoryUiState
    data object Loading: StoryUiState
}


class StoryViewModel(): ViewModel(){
    var storyUiState: StoryUiState by mutableStateOf<StoryUiState>(StoryUiState.Initial)
    var selectedLanguage by mutableStateOf("Turkish")
    var userPrompt by mutableStateOf("")



    fun updateLanguage(language: String) {
        selectedLanguage = language

    }

    fun updatePrompt(prompt: String) {
        userPrompt = prompt

    }

    fun createStory(prompt:String,translateTo:String){
        viewModelScope.launch {
            var storiesApi: StoryApi? = null
            storyUiState = StoryUiState.Loading
            try {
                storiesApi = StoryApi.getInstance()
                val requestBody = StoryRequest(prompt,translateTo)
                val generatedStory = storiesApi.createStory(requestBody)

                storyUiState = StoryUiState.Success(generatedStory)
            }catch (e: Exception) {
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No Internet Connection"
                    is retrofit2.HttpException -> {
                        // Extract error body for more detailed error information
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.d("errorBody",errorBody.toString())
                        try {
                            // If the error response is in JSON format, you might want to parse it
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
                storyUiState = StoryUiState.Error(errorMessage)
            }
        }
    }


}