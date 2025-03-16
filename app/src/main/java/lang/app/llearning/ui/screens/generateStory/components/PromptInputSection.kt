package lang.app.llearning.ui.screens.generateStory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import lang.app.llearning.R
import lang.app.llearning.viewmodel.StoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromptInputSection(storyViewModel: StoryViewModel, modifier: Modifier, selectedLanguage: String){

    var userInput = storyViewModel.userPrompt
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(12.dp)
    ) {
        TextField(
            value = userInput,
            onValueChange = {userInput = it
                storyViewModel.updatePrompt(it)},

            label = { Text(stringResource(R.string.user_prompt_label)) },
            placeholder = { Text(stringResource(R.string.user_prompt_placeholder)) },
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .weight(1f)
        )
        Spacer(modifier = modifier.width(6.dp))
        PromptSubmitButton(modifier,storyViewModel,userInput,selectedLanguage,{keyboardController?.hide()})
    }


}