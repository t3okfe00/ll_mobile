package lang.app.llearning.ui.screens.generateStory

import lang.app.llearning.viewmodel.StoryUiState
import lang.app.llearning.viewmodel.StoryViewModel
import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import lang.app.llearning.R
import lang.app.llearning.data.model.Story
import lang.app.llearning.ui.screens.LoadingScreen
import lang.app.llearning.ui.theme.AppTheme
import lang.app.llearning.viewmodel.TextToSpeechUiState
import lang.app.llearning.viewmodel.TextToSpeechViewModel


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

@Composable
fun StoryRenderer(modifier: Modifier = Modifier, uiState: StoryUiState) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (uiState is StoryUiState.Error) {
        LaunchedEffect(uiState) {
            scope.launch {
                snackbarHostState.showSnackbar(uiState.message)
            }
        }
    }

    when(uiState){
        is StoryUiState.Loading -> LoadingScreen(modifier)
        is StoryUiState.Success -> StorySection(modifier, story = uiState.story, textToSpeechViewModel = viewModel())

        is StoryUiState.Error -> {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = modifier
            )
        }
        else -> {
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageDropdown(storyViewModel: StoryViewModel, modifier: Modifier = Modifier,
                     selectedLanguage:String,
                     onLanguageSelected:(String) ->Unit) {
    var expanded by remember { mutableStateOf(false) }

    val languages = listOf("English", "Spanish", "French", "German", "Italian","Finnish","Turkish","Russian")

    Box(modifier = modifier.padding(16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedLanguage,
                onValueChange = {},
                readOnly = true,
                label = { Text(selectedLanguage) },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp)
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(text = language) },
                        onClick = {
                            onLanguageSelected(language)
                            storyViewModel.updateLanguage(language)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

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

            label = {Text(stringResource(R.string.user_prompt_label))},
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

@Composable
fun PromptSubmitButton(modifier: Modifier=Modifier,
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




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorySection(
    modifier: Modifier = Modifier,
    story: Story,
    textToSpeechViewModel: TextToSpeechViewModel
) {
    val (englishStory, translatedStory) = story
    val englishStorySplitted = splitStoryIntoSentences(englishStory)
    val translatedStorySplitted = splitStoryIntoSentences(translatedStory)
    var highlightedIndex by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val ttsUiState = textToSpeechViewModel.ttsUiState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentlyFetchingSentenceIndex by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(10.dp)) {

            if (ttsUiState is TextToSpeechUiState.Error) {
                LaunchedEffect(key1 = ttsUiState) {
                    snackbarHostState.showSnackbar(ttsUiState.message)
                }
            }

            // English story sentences
            englishStorySplitted.forEachIndexed { index, sentence ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                            .combinedClickable(
                                onClick = {
                                    highlightedIndex = index
                                },
                                onLongClick = {
                                    currentlyFetchingSentenceIndex = index
                                    coroutineScope.launch {
                                        textToSpeechViewModel.fetchAndPlayAudio(sentence, context)
                                    }
                                }
                            )
                            .background(
                                if (highlightedIndex == index) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .align(Alignment.CenterStart)
                    )

                    if (currentlyFetchingSentenceIndex == index && textToSpeechViewModel.ttsUiState is TextToSpeechUiState.Loading) {
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

            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                thickness = 2.dp,
                modifier = modifier.padding(vertical = 1.dp)
            )

            // Translated story sentences
            translatedStorySplitted.forEachIndexed { index, sentence ->
                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = sentence,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = modifier
                            .combinedClickable(
                                onClick = {
                                    highlightedIndex = index
                                },
                                onLongClick = {
                                    currentlyFetchingSentenceIndex = index
                                    coroutineScope.launch {
                                        textToSpeechViewModel.fetchAndPlayAudio(sentence, context)
                                    }
                                }
                            )
                            .background(
                                if (highlightedIndex == index) MaterialTheme.colorScheme.inversePrimary
                                else MaterialTheme.colorScheme.onSecondary
                            )
                            .align(Alignment.CenterStart)
                    )

                    if (currentlyFetchingSentenceIndex == index && textToSpeechViewModel.ttsUiState is TextToSpeechUiState.Loading) {
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = modifier
        )
    }
}


fun splitStoryIntoSentences(story:String) : List<String>{
    return story.split(Regex("(?<=[.!?])\\s+"))
}




