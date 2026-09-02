package com.example.model

data class Surah(
    val number: Int,
    val nameArabic: String,
    val nameEnglish: String,
    val nameBengali: String,
    val englishMeaning: String,
    val bengaliMeaning: String,
    val totalVerses: Int,
    val revelationType: String, // "Meccan" or "Medinan"
    val startPage: Int = 1
)

data class Ayah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val textArabic: String,
    val pronunciationBn: String, // Bengali pronunciation (উচ্চারণ)
    val translationBn: String, // Bengali meaning (বাংলা অনুবাদ)
    val transliteration: String = "", // English transliteration
    val translationEn: String = "",
    val isBookmarked: Boolean = false
)

data class QuranReciter(
    val id: String,
    val name: String,
    val subName: String,
    val style: String,
    val baseUrl: String
)

data class QuranAudioState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentSurahNumber: Int? = null,
    val currentAyahNumber: Int? = null,
    val reciterIndex: Int = 0,
    val isFullSurahMode: Boolean = true,
    val progressMs: Long = 0L,
    val durationMs: Long = 0L
)
