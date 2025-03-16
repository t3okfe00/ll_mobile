package lang.app.llearning.model

import AuthInterceptor
import TokenManager

import lang.app.llearning.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val BASE_URL = BuildConfig.BASE_URL

data class Story(
    var englishStory: String,
    var translatedStory: String
)

data class StoryRequest(val prompt: String,val translateTo: String)


interface  StoryApi {

    @POST("story")
    suspend fun createStory(@Body requestBody: StoryRequest) : Story

    companion object {
        private var storyService: StoryApi? = null
        fun getInstance(): StoryApi {

            val tokenManager = TokenManager

            if (storyService === null) {
                val client = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(tokenManager))
                    .build()

                storyService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(StoryApi::class.java)
            }
            return storyService!!
        }
    }

}
