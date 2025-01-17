package lang.app.llearning.data.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Replace with your actual base URL
const val BASE_URL_TTS = "http://192.168.1.110:3000/"

data class TextToSpeechRequest(
    val text: String
)

data class TextToSpeechResponse(
    val audioContent: String // Assuming the backend returns base64-encoded audio content
)

interface TextToSpeechApi {
    @POST("text-to-speech")
    suspend fun synthesizeSpeech(@Body request: TextToSpeechRequest): TextToSpeechResponse

    companion object {
        var ttsService: TextToSpeechApi? = null
        fun getInstance(): TextToSpeechApi {
            if (ttsService == null) {
                ttsService = Retrofit.Builder()
                    .baseUrl(BASE_URL_TTS)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(TextToSpeechApi::class.java)
            }
            return ttsService!!
        }
    }
}
