data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: ContentResult
)

data class ContentResult(
    val parts: List<ResultPart>
)

data class ResultPart(
    val text: String
)
