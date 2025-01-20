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

sealed interface StoryUiState {
    data class Success(val story: Story): StoryUiState
    object Initial: StoryUiState
    object Error: StoryUiState
    object Loading: StoryUiState
}


class StoryViewModel(private val savedStateHandle:SavedStateHandle): ViewModel(){
    var storyUiState: StoryUiState by mutableStateOf<StoryUiState>(StoryUiState.Initial)
    private var _story: Story? = null
    var selectedLanguage by mutableStateOf("Select a Language")
    var userPrompt by mutableStateOf("")


    val story: Story?
        get() = _story
    init {
        storyUiState = StoryUiState.Initial
        selectedLanguage = savedStateHandle["selectedLanguage"] ?: "Select a Language"
        userPrompt = savedStateHandle["userPrompt"] ?: ""

    }

    fun updateLanguage(language: String) {
        selectedLanguage = language
        savedStateHandle["selectedLanguage"] = language  // Save to savedStateHandle
    }

    fun updatePrompt(prompt: String) {
        userPrompt = prompt
        savedStateHandle["userPrompt"] = prompt  // Save to savedStateHandle
    }

    fun createStory(prompt:String,translateTo:String){
        viewModelScope.launch {
            var storiesApi: StoryApi? = null
            storyUiState = StoryUiState.Loading
            try {
                storiesApi = StoryApi.getInstance()
                val requestBody = StoryRequest(prompt,translateTo)
                val generatedStory = storiesApi.createStory(requestBody)
                Log.d("Generated Story",generatedStory.toString())
                _story = generatedStory
                storyUiState = StoryUiState.Success(generatedStory)
            }catch (e: Exception){
                Log.d("ERROR",e.message.toString())
                storyUiState = StoryUiState.Error
            }
        }
    }


}