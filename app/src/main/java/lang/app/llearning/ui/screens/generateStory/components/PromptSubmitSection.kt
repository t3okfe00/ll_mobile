package lang.app.llearning.ui.screens.generateStory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lang.app.llearning.R
import lang.app.llearning.viewmodel.StoryUiState
import lang.app.llearning.viewmodel.StoryViewModel

@Composable
fun PromptSubmitButton(modifier: Modifier = Modifier,
                       storyViewModel: StoryViewModel,
                       userInput:String,
                       selectedLanguage: String,
                       onSubmit:()->Unit){
    val uiState = storyViewModel.storyUiState
    Box {
        if(uiState is StoryUiState.Loading){
            CircularProgressIndicator(
                modifier = modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        else{
            IconButton(
                onClick = {
                    storyViewModel.createStory(userInput, selectedLanguage)
                    onSubmit()},
            ) {
                Icon(
                    painter = painterResource(R.drawable.wizard),
                    contentDescription = stringResource(R.string.wand_icon),
                    tint = Color.Unspecified,
                    modifier = modifier
                        .size(48.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                )
            }
        }
    }

}