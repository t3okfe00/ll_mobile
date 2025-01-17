import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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


class StoryViewModel: ViewModel(){
    var storyUiState: StoryUiState by mutableStateOf<StoryUiState>(StoryUiState.Initial)
    init {
        storyUiState = StoryUiState.Initial
    }

    fun createStory(prompt:String,translateTo:String){
        viewModelScope.launch {
            var storiesApi: StoryApi? = null
            storyUiState = StoryUiState.Loading
            try {
                storiesApi = StoryApi.getInstance()
                val requestBody = StoryRequest(prompt,translateTo)
                storyUiState = StoryUiState.Success(storiesApi.createStory(requestBody))
            }catch (e: Exception){
                Log.d("ERROR",e.message.toString())
                storyUiState = StoryUiState.Error
            }
        }
    }


}