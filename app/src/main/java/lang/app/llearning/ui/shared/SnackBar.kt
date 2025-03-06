import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun SnackBar(message: String, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(key1 = message) {
        snackbarHostState.showSnackbar(message)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.Center)  // Centers in the Box
                .padding(16.dp)
                .zIndex(1f),
            snackbar = { data ->
                Snackbar(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = data.visuals.message)
                }
            }
        )
    }
}