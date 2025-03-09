package lang.app.llearning.ui.screens
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import lang.app.llearning.ui.theme.AppTheme
import lang.app.llearning.viewmodel.AuthUiState
import lang.app.llearning.viewmodel.AuthViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    modifier: Modifier,
    onStoriesClick: () -> Unit,
    onBuyTokensClick: () -> Unit,
    authViewModel: AuthViewModel

) {
    val uiState = authViewModel.authUiState

    when(uiState){
        is AuthUiState.Success ->{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello,${uiState.tokenResponse.userEmail}!",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(onClick = onStoriesClick,
                    colors =ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) ) {
                    BasicText(text = "My Stories")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onBuyTokensClick,
                    colors =ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.primaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )) {
                    BasicText(text = "Buy Tokens")
                }
            }
        }
        is AuthUiState.Loading -> {
            CircularProgressIndicator()
        }
        else->{
            Column(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("You need to sign in to continue")
                GoogleSignInButton(authViewModel)
            }
        }
    }



}


