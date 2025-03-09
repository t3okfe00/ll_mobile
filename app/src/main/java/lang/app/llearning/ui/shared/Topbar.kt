package lang.app.llearning.ui.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import lang.app.llearning.ui.screens.TopBar

@Composable
fun Topbar(modifier:Modifier){
    TopBar(modifier, tokens = 3000, storiesCreated = 23)
}