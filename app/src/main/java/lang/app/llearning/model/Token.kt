package lang.app.llearning.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


data class Token(
    val accessToken: String,
    val refreshToken: String,
    val userEmail: String?
)

data class LoginRequestBody(val token: String)

interface AuthApi {

    @POST("auth/google-login")
    suspend fun login(@Body requestBody: LoginRequestBody): Token

    companion object {
        var authService: AuthApi? = null

        fun getInstance(): AuthApi {
            if (authService == null) {
                authService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(AuthApi::class.java)
            }
            return authService!!
        }
    }
}