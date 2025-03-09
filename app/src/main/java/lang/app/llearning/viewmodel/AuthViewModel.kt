package lang.app.llearning.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import lang.app.llearning.data.model.AuthApi
import lang.app.llearning.data.model.LoginRequestBody
import lang.app.llearning.data.model.Token

sealed interface AuthUiState {
    object Idle : AuthUiState
    object Loading : AuthUiState
    data class Success(val tokenResponse: Token) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel: ViewModel() {
    var authUiState : AuthUiState by mutableStateOf<AuthUiState>(AuthUiState.Idle)


    fun loginWithGoogleId(googleIdToken: String){
        viewModelScope.launch {
            var authApi: AuthApi? = null
            authUiState = AuthUiState.Loading
            try {
                authApi = AuthApi.getInstance();

                val loginRequestBody = LoginRequestBody(googleIdToken);
                val loginResponse = authApi.login(loginRequestBody);
                authUiState = AuthUiState.Success(loginResponse)


            }catch (e: Exception){
                val errorMessage = when (e) {
                    is java.net.UnknownHostException -> "No Internet Connection"
                    is retrofit2.HttpException -> {

                        val errorBody = e.response()?.errorBody()?.string()

                        "API error: ${e.message}"
                    }
                    else -> "An unexpected error occurred, please try again later"
                }


                authUiState = AuthUiState.Error(errorMessage)

            }

        }

    }



}