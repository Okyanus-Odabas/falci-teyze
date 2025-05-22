package com.example.falciteyze

data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val inlineData: InlineData? = null,
    val text: String? = null
)

data class InlineData(
    val mimeType: String,
    val data: String
)

data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)
