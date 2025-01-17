package lang.app.llearning.data.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

const val BASE_URL = "http://192.168.1.110:3000/"
data class Success(val story: Story)

data class Story(
    var englishStory: String,
    var translatedStory: String,
    var tokenUsed: Int
)


interface  StoryApi {
    @POST("story")
    suspend fun createStory(@Body requestBody: StoryRequest) : Story


    companion object {
        var storyService: StoryApi? = null
        fun getInstance(): StoryApi {
            if (storyService === null) {
                storyService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(StoryApi::class.java)
            }
            return storyService!!
        }
    }

}
