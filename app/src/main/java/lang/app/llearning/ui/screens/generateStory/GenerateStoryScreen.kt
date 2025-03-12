package lang.app.llearning.ui.screens.generateStory
import lang.app.llearning.viewmodel.StoryViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lang.app.llearning.R



@Composable
fun GenerateStoryScreen(modifier: Modifier = Modifier,storyViewModel: StoryViewModel = viewModel()){
    val uiState = storyViewModel.storyUiState
    var selectedLanguage =storyViewModel.selectedLanguage
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(horizontal = 8.dp)
        ){
            Text(
                text = stringResource(R.string.generate_story_screen_heading),
                modifier = modifier,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
            LanguageDropdown(
                storyViewModel = storyViewModel,
                modifier = Modifier.align(Alignment.CenterVertically),
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it }
            )
        }
        PromptInputSection(storyViewModel,modifier,selectedLanguage)
        StoryRenderer(modifier, uiState)
    }
}



fun splitStoryIntoSentences(story:String) : List<String>{
    return story.split(Regex("(?<=[.!?])\\s+"))
}




