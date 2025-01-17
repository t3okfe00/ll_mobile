package lang.app.llearning.data.model

data class StoryResponse(
    val englishStory: String,
    val translatedStory: String,
    val tokenUsed: Int,
)
