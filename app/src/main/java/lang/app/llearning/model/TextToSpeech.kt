package lang.app.llearning.model

import AuthInterceptor
import TokenManager
import lang.app.llearning.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Replace with your actual base URL
const val BASE_URL_TTS = BuildConfig.BASE_URL

data class TextToSpeechRequest(
    val text: String
)

data class TextToSpeechResponse(
    val url: String
)

interface TextToSpeechApi {
    @POST("text-to-speech")
    suspend fun fetchAudio(@Body request: TextToSpeechRequest): TextToSpeechResponse

    companion object {
        var ttsService: TextToSpeechApi? = null
         val tokenManager= TokenManager

        fun getInstance(): TextToSpeechApi {
            if (ttsService == null) {

                val client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(tokenManager))
                    .build()

                ttsService = Retrofit.Builder()
                    .baseUrl(BASE_URL_TTS)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TextToSpeechApi::class.java)
            }
            return ttsService!!
        }
    }
}
