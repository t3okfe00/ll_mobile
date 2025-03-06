import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ErrorScreen(modifier: Modifier = Modifier, message:String) {
    Text(
        text=message,
        color = MaterialTheme.colorScheme.error
    )

}