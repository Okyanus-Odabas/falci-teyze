package com.example.falciteyze

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel

object GeminiService {
    fun getModel(context: Context): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY
        )
    }
}