package lang.app.llearning.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "This is main screen",
            modifier = modifier,
            color = MaterialTheme.colorScheme.primary
        )
        GoogleSignInButton()
    }
}

@Composable
fun GoogleSignInButton(){
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val onClick: ()->Unit = {

        val credentialManager = CredentialManager.create(context)

        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }

        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(true)
            .setServerClientId("754856860469-s14k0nea8ic6vff9fhamven6r3t7ua7a.apps.googleusercontent.com")
            .setAutoSelectEnabled(true)
            .setNonce(hashedNonce)
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {

                val result = credentialManager.getCredential(
                    request = request,
                    context= context
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                Toast.makeText(context,"You are signed in",Toast.LENGTH_SHORT).show()
            }catch (e: GetCredentialException){

                Toast.makeText(context,"Error: ${e.message}",Toast.LENGTH_SHORT).show()

            }catch (e: GoogleIdTokenParsingException){
                Toast.makeText(context,"Error: ${e.message}",Toast.LENGTH_SHORT).show()

            }
        }
    }

    Button(
        onClick = onClick
    ) {
        Text("Sign in with Google")
    }
}