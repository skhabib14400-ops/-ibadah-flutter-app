package com.example.model

enum class DuaCategory(val id: String, val titleEn: String, val titleBn: String, val icon: String) {
    MORNING_EVENING("morning_evening", "Morning & Evening", "সকাল ও সন্ধ্যার যিকির", "wb_sunny"),
    PRAYER_SALAH("prayer_salah", "Prayer & Adhan", "নামাজ ও আজান", "mosque"),
    FASTING_RAMADAN("fasting_ramadan", "Fasting & Ramadan", "রোজা ও রমজান", "bedtime"),
    DAILY_LIFE("daily_life", "Daily Life & Routine", "দৈনন্দিন জীবন", "home"),
    TRAVEL_JOURNEY("travel_journey", "Travel & Journey", "সফর ও যাত্রা", "flight"),
    PROTECTION_RUQYAH("protection_ruqyah", "Protection & Cure", "সুরক্ষা ও আরোগ্য", "shield"),
    FORGIVENESS_TAWBAH("forgiveness_tawbah", "Forgiveness (Tawbah)", "ক্ষমা ও তাওবা", "favorite"),
    QURANIC_RABBANAS("quranic_rabbanas", "Quranic Rabbanas", "কুরআনের ৪০ রব্বানা", "menu_book")
}

data class IslamicDua(
    val id: String,
    val category: DuaCategory,
    val titleEn: String,
    val titleBn: String,
    val arabicText: String,
    val transliteration: String,
    val translationEn: String,
    val translationBn: String,
    val reference: String,
    val virtues: String? = null,
    val repeatCount: Int = 1,
    var isFavorite: Boolean = false
)

data class AsmaName(
    val id: Int,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningBn: String,
    val benefitEn: String,
    val benefitBn: String
)

data class IslamicVideo(
    val id: String,
    val title: String,
    val speakerOrChannel: String,
    val duration: String,
    val isLive: Boolean = false,
    val category: String, // "Live", "Quran", "Lecture", "Dua"
    val streamUrlOrEmbed: String,
    val location: String? = null
)
