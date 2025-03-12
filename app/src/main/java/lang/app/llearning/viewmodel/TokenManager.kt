import android.content.Context
import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val ACCESS_TOKEN_KEY = "access_token"
    private const val REFRESH_TOKEN_KEY = "refresh_token"
    private const val USER_EMAIL_KEY = "user_email"
    private lateinit var prefs: SharedPreferences


    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    var accessToken: String?
        get() = prefs.getString(ACCESS_TOKEN_KEY, null)
        set(value) = prefs.edit().putString(ACCESS_TOKEN_KEY, value).apply()

    var refreshToken: String?
        get() = prefs.getString(REFRESH_TOKEN_KEY, null)
        set(value) = prefs.edit().putString(REFRESH_TOKEN_KEY, value).apply()

    var userEmail: String?
        get() = prefs.getString(USER_EMAIL_KEY, null)
        set(value) = prefs.edit().putString(USER_EMAIL_KEY, value).apply()

    fun clearTokens() {
        prefs.edit().clear().apply()
    }
}




class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        tokenManager.accessToken?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(request.build())
    }
}