package lang.app.llearning.viewmodel

import TokenManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lang.app.llearning.model.AuthApi
import lang.app.llearning.model.LoginRequestBody
import lang.app.llearning.model.Token



sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val tokenResponse: Token) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel: ViewModel() {
    var authUiState : AuthUiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)

    private val tokenManager = TokenManager


    init {

        if (tokenManager.accessToken != null && tokenManager.refreshToken != null) {
            authUiState = AuthUiState.Success(Token(tokenManager.accessToken!!, tokenManager.refreshToken!!,tokenManager.userEmail))
        } else {
            authUiState = AuthUiState.Idle
        }
    }

    fun loginWithGoogleId(googleIdToken: String,storyViewModel: StoryViewModel){
        viewModelScope.launch {
            var authApi: AuthApi? = null
            authUiState = AuthUiState.Loading
            try {
                authApi = AuthApi.getInstance();

                val loginRequestBody = LoginRequestBody(googleIdToken);
                val loginResponse = authApi.login(loginRequestBody);
                authUiState = AuthUiState.Success(loginResponse)

                tokenManager.accessToken = loginResponse.accessToken
                tokenManager.refreshToken = loginResponse.refreshToken
                tokenManager.userEmail = loginResponse.userEmail

                storyViewModel.resetState()

            }catch (e: Exception){
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No Internet Connection"
                    is retrofit2.HttpException -> {
                        "API error: ${e.message}"
                    }
                    else -> "An unexpected error occurred, please try again later"
                }
                authUiState = AuthUiState.Error(errorMessage)
            }

        }

    }
    fun logout() {
        tokenManager.clearTokens()
    }
}




