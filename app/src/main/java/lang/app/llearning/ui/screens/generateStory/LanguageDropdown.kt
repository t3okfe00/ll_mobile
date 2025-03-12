package lang.app.llearning.ui.screens.generateStory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lang.app.llearning.viewmodel.StoryViewModel

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