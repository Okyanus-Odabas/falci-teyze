package com.example.falciteyze

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import com.airbnb.lottie.LottieAnimationView
import android.widget.ScrollView
import android.widget.Toast

class RuyaFragment : Fragment() {

    private lateinit var ruyaEditText: TextInputEditText
    private lateinit var gonderButton: Button
    private lateinit var sonucText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_ruya, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scrollView = view.findViewById<ScrollView>(R.id.ruyaScrollView)
        val loadingOverlay = view.findViewById<ConstraintLayout>(R.id.loadingOverlay)
        val loadingAnimation = view.findViewById<LottieAnimationView>(R.id.loadingAnimation)

        ruyaEditText = view.findViewById(R.id.nameEditText)
        gonderButton = view.findViewById(R.id.gonderButton)
        sonucText = view.findViewById(R.id.sonucText)

        gonderButton.setOnClickListener {
            val ruyaMetni = ruyaEditText.text.toString().trim()

            if (ruyaMetni.isNotEmpty()) {
                val prompt = buildRuyaPrompt(ruyaMetni)

                val partText = Part(text = prompt)
                val content = Content(parts = listOf(partText))
                val request = GeminiRequest(contents = listOf(content))

                loadingOverlay.visibility = View.VISIBLE
                loadingAnimation.playAnimation()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://generativelanguage.googleapis.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(GeminiApiService::class.java)
                val apiKey = BuildConfig.GEMINI_API_KEY

                api.generateContent(apiKey, request).enqueue(object : Callback<GeminiResponse> {
                    override fun onResponse(
                        call: Call<GeminiResponse>,
                        response: Response<GeminiResponse>
                    ) {
                        val result = response.body()
                            ?.candidates?.firstOrNull()
                            ?.content?.parts?.firstOrNull()?.text

                        sonucText.text = result ?: "Yanıt alınamadı."

                        finishLoading(loadingOverlay, loadingAnimation, scrollView)
                    }

                    override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                        Log.e("GeminiHata", "Bağlantı hatası: ${t.message}")
                        Toast.makeText(requireContext(), "Bağlantı hatası", Toast.LENGTH_SHORT).show()
                        finishLoading(loadingOverlay, loadingAnimation, scrollView)
                    }
                })
            } else {
                Toast.makeText(requireContext(), "Lütfen rüyanı yaz", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buildRuyaPrompt(ruyaMetni: String): String {
        return """
            Bir kullanıcı bana şu rüyayı anlattı:
            
            "$ruyaMetni"
            
            Bu rüyanın anlamı nedir? Geleneksel rüya tabirleri açısından samimi bir şekilde yorumla.
            Halk arasında bilinen anlamlara da başvurabilirsin. 
            Açık ve anlaşılır bir dille yaz, yorum yaparken empatik ol, gizemli ama korkutmayan bir ton kullan.
        """.trimIndent()
    }

    private fun finishLoading(
        overlay: ConstraintLayout,
        animation: LottieAnimationView,
        scrollView: ScrollView
    ) {
        animation.cancelAnimation()
        overlay.visibility = View.GONE

        val yKonumu = sonucText.top - 100
        ObjectAnimator.ofInt(scrollView, "scrollY", yKonumu).apply {
            duration = 1500
            start()
        }
    }
}
