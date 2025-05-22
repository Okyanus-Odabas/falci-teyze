# ☕ Falcı Teyze - AI Destekli Fal ve Rüya Yorumu Uygulaması

Falcı Teyze, kahve falı ve rüya yorumlarını yapay zeka destekli bir şekilde sunan eğlenceli ve kişiselleştirilmiş bir Android uygulamasıdır. Kullanıcılar, kahve fincanı fotoğrafı yükleyerek veya rüyalarını yazarak yapay zekadan detaylı ve geleneksel üslupla yazılmış fal yorumları alabilirler.

---

## 🚀 Özellikler

- 📷 Kahve fincanı fotoğrafı yükleyerek AI yorumları alma
- 🌙 Rüya metni girerek geleneksel rüya tabirleriyle anlam analizi
- 🎭 Renk, figür ve sembol analizi
- 📅 Doğum tarihi, saati ve cinsiyet gibi kişisel bilgilere göre özelleştirilmiş içerik
- 🔮 Astrolojik olayları yorumlara entegre etme (opsiyonel)
- 🎨 Özgün arayüz tasarımı ve Lottie animasyon desteği

---

## 📸 Ekran Görüntüleri

> (İstersen buraya birkaç ekran görüntüsü ekleyebilirsin)

---

## 🛠️ Kurulum

### 1. Projeyi Klonla:
```bash
git clone https://github.com/kullaniciadi/FalciTeyze.git
```

### 2. API Anahtarını Ekleyin

Projenin kök dizinine `local.properties` dosyasına aşağıdaki satırı ekleyin:

```
GEMINI_API_KEY=your_google_generative_ai_api_key
```

> ⚠️ `local.properties` dosyası `.gitignore` ile korunmaktadır. GitHub'a yüklenmemelidir!

### 3. Android Studio ile açın ve çalıştırın.

---

## 📦 Kullanılan Teknolojiler

- Kotlin + Android SDK
- Retrofit (REST API)
- Google Generative AI (Gemini API)
- Lottie (Yükleme animasyonu)
- ViewBinding & ConstraintLayout

---

## 🤖 Yapay Zeka Destekli Yorum Motoru

Uygulama, Google Gemini API ile kahve falı ve rüya metinlerini analiz eder. Kullanıcının verdiği kişisel bilgiler ve sembollere dayalı olarak geleneksel Türk falcılığı tarzında yorumlar üretir.

---

## 🧠 Geliştirici Notu

Bu proje bireysel bir geliştirici çalışmasıdır ve eğlence amaçlıdır. API anahtarınızı güvenli bir şekilde saklamayı unutmayın.

---

## 🧑‍💻 Geliştiren

**[@okyanus-odabas](https://github.com/Okyanus-Odabas)**  
Android Developer

---

## 📄 License

Custom License – No commercial use allowed.  
© 2025 Okyanus Odabaş. All rights reserved.
