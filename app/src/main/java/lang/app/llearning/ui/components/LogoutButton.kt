package lang.app.llearning.ui.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun LogoutButton(navController: NavController) {
    Button(
        onClick = {
            TokenManager.clearTokens()
            navController.navigate("login") {
                popUpTo("home") { inclusive = true } // Clears navigation history
            }
        }
    ) {
        Text("Logout")
    }
}
