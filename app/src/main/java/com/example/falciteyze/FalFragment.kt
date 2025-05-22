package com.example.falciteyze

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import android.animation.ObjectAnimator
import android.widget.ScrollView

class FalFragment : Fragment() {

    private lateinit var birthdayButton: Button
    private lateinit var birthdayText: TextView
    private lateinit var timeText: TextView
    private lateinit var kullaniciIsmi: String
    private lateinit var dogumGunu: String
    private lateinit var dogumSaati: String
    private lateinit var genderSpinner: Spinner
    private lateinit var yuklenenfotoView: ImageView
    private lateinit var fotoYukle: ImageButton
    private lateinit var nameEditText: TextInputEditText
    private lateinit var startButton: Button
    private lateinit var resultText: TextView
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            yuklenenfotoView.setImageURI(it)
            fotoYukle.alpha = 0f
            fotoYukle.isClickable = true
            fotoYukle.isFocusable = true
            yuklenenfotoView.setBackgroundResource(0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_fal, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val scrollView = view.findViewById<ScrollView>(R.id.falScrollView)
        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        resultText = view.findViewById(R.id.sonucText)
        birthdayButton = view.findViewById(R.id.birthdayButton)
        birthdayText = view.findViewById(R.id.birthdayText)
        timeText = view.findViewById(R.id.timeText)
        yuklenenfotoView = view.findViewById(R.id.yuklenenfotoView)
        fotoYukle = view.findViewById(R.id.fotoYukle)
        genderSpinner = view.findViewById(R.id.genderSpinner)
        nameEditText = view.findViewById(R.id.nameEditText)
        startButton = view.findViewById(R.id.gonderButton)

        val args: FalFragmentArgs by navArgs()
        val falciId = args.falciId
        val loadingOverlay = view.findViewById<ConstraintLayout>(R.id.loadingOverlay2)
        val loadingAnimation = view.findViewById<LottieAnimationView>(R.id.loadingAnimation2)

        setupGenderSpinner()

        fotoYukle.setOnClickListener { imagePickerLauncher.launch("image/*") }
        birthdayButton.setOnClickListener { showDatePickerDialog() }
        view.findViewById<Button>(R.id.timeButton).setOnClickListener { showTimePickerDialog() }

        startButton.setOnClickListener {
            handleFalButtonClick(falciId, scrollView, loadingOverlay, loadingAnimation)
        }
    }

    private fun setupGenderSpinner() {
        val genderOptions = listOf("Cinsiyet seçiniz", "Kadın", "Erkek")
        val adapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            genderOptions
        ) {
            override fun isEnabled(position: Int): Boolean = true

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                view.setTextColor(if (position == 0) Color.GRAY else Color.BLACK)
                view.typeface = ResourcesCompat.getFont(
                    context,
                    if (position == 0) R.font.poppins_light else R.font.poppins_extrabold
                )
                return view
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                val text = getItem(position)?.trim()?.lowercase()
                view.setTextColor(if (text == "cinsiyet seçiniz") Color.GRAY else Color.WHITE)
                view.typeface = ResourcesCompat.getFont(
                    context,
                    if (text == "cinsiyet seçiniz") R.font.poppins_light else R.font.poppins_extrabold
                )
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderSpinner.adapter = adapter
    }

    private fun handleFalButtonClick(
        falciId: String,
        scrollView: ScrollView,
        loadingOverlay: ConstraintLayout,
        loadingAnimation: LottieAnimationView
    ) {
        val isim = nameEditText.text.toString().trim()
        val dogum = birthdayText.text.toString().trim()
        val saat = timeText.text.toString().trim()
        val secilenCinsiyet = genderSpinner.selectedItem.toString()

        if (isim.isNotEmpty() && dogum != "Henüz seçilmedi" && selectedImageUri != null && secilenCinsiyet != "Cinsiyet seçiniz") {
            kullaniciIsmi = isim
            dogumGunu = dogum
            dogumSaati = saat
            loadingOverlay.visibility = View.VISIBLE
            loadingAnimation.playAnimation()

            val base64Image = uriToBase64(selectedImageUri!!)
            if (base64Image == null) {
                Toast.makeText(requireContext(), "Görsel dönüştürülemedi", Toast.LENGTH_SHORT).show()
                return
            }

            val prompt = generatePrompt(
                falciId = falciId,
                kullaniciIsmi = kullaniciIsmi,
                dogumGunu = dogumGunu,
                dogumSaati = dogumSaati,
                cinsiyet = secilenCinsiyet
            )
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(inlineData = InlineData("image/jpeg", base64Image)),
                            Part(text = prompt)
                        )
                    )
                )
            )

            val retrofit = Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(GeminiApiService::class.java)
            val apiKey = BuildConfig.GEMINI_API_KEY

            api.generateContent(apiKey, request).enqueue(object : Callback<GeminiResponse> {
                override fun onResponse(call: Call<GeminiResponse>, response: Response<GeminiResponse>) {
                    val result = response.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    resultText.text = result ?: "Boş yanıt geldi."
                    finishLoading(loadingOverlay, loadingAnimation, scrollView)
                }

                override fun onFailure(call: Call<GeminiResponse>, t: Throwable) {
                    Log.e("GeminiHata", "Bağlantı hatası: ${t.message}")
                    Toast.makeText(requireContext(), "Bağlantı hatası", Toast.LENGTH_SHORT).show()
                    finishLoading(loadingOverlay, loadingAnimation, scrollView)
                }
            })
        } else {
            Toast.makeText(requireContext(), "Lütfen tüm alanları doldurun ve fotoğraf seçin", Toast.LENGTH_SHORT).show()
        }
    }

    private fun finishLoading(overlay: ConstraintLayout, animation: LottieAnimationView, scrollView: ScrollView) {
        animation.cancelAnimation()
        overlay.visibility = View.GONE
        val yKonumu = resultText.top - 100
        ObjectAnimator.ofInt(scrollView, "scrollY", yKonumu).apply {
            duration = 1500
            start()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, y, m, d -> birthdayText.text = "$d/${m + 1}/$y" },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            requireContext(),
            { _, h, m -> timeText.text = String.format("%02d:%02d", h, m) },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var bytesRead: Int
            while (inputStream?.read(buffer).also { bytesRead = it ?: -1 } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            val imageBytes = outputStream.toByteArray()
            Base64.encodeToString(imageBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
