package com.example.model

import java.util.Calendar

enum class PrayerType(val displayName: String, val arabicName: String, val bengaliName: String) {
    FAJR("Fajr", "الفجر", "ফজর"),
    SUNRISE("Sunrise", "الشروق", "সূর্যোদয়"),
    ISHRAQ("Ishraq", "الإشراق", "ইশরাক"),
    CHASHT("Chasht (Duha)", "الضحى", "চাশত / দুহা"),
    DHUHR("Dhuhr", "الظهر", "যোহর"),
    ASR("Asr", "العصر", "আসর"),
    SUNSET("Sunset", "الغروب", "সূর্যাস্ত"),
    MAGHRIB("Maghrib", "المغرب", "মাগরিব"),
    ISHA("Isha", "العشاء", "ইশা"),
    TAHAJJUD("Tahajjud", "التهجد", "তাহাজ্জুদ"),
    SAHRI_END("Sahri End", "نهاية السحور", "সাহরি শেষ"),
    IFTAR("Iftar", "الإفطار", "ইফতার")
}

data class SinglePrayerSchedule(
    val type: PrayerType,
    val timeMillis: Long,
    val timeFormatted: String, // e.g., "04:35 AM"
    val isForbidden: Boolean = false,
    val isCurrent: Boolean = false,
    val isNext: Boolean = false
)

data class ForbiddenPeriod(
    val name: String,
    val arabicName: String,
    val bengaliName: String,
    val startTimeFormatted: String,
    val endTimeFormatted: String,
    val startMillis: Long,
    val endMillis: Long,
    val description: String
)

data class DailyPrayerSummary(
    val dateString: String,
    val hijriDateString: String,
    val fajr: SinglePrayerSchedule,
    val sunrise: SinglePrayerSchedule,
    val ishraq: SinglePrayerSchedule,
    val chasht: SinglePrayerSchedule,
    val zawalStart: String,
    val zawalEnd: String,
    val dhuhr: SinglePrayerSchedule,
    val asr: SinglePrayerSchedule,
    val sunset: SinglePrayerSchedule,
    val maghrib: SinglePrayerSchedule,
    val isha: SinglePrayerSchedule,
    val tahajjud: SinglePrayerSchedule,
    val sahriEndToday: SinglePrayerSchedule,
    val iftarToday: SinglePrayerSchedule,
    val sahriEndTomorrow: SinglePrayerSchedule,
    val iftarTomorrow: SinglePrayerSchedule,
    val forbiddenPeriods: List<ForbiddenPeriod>,
    val currentActivePrayer: SinglePrayerSchedule?,
    val nextUpcomingPrayer: SinglePrayerSchedule?,
    val remainingMillisToNextPrayer: Long,
    val elapsedFractionCurrentPrayer: Float,
    val remainingMillisToSahri: Long,
    val remainingMillisToIftar: Long,
    val isFastingHours: Boolean,
    val isCurrentlyForbidden: Boolean,
    val currentForbiddenPeriod: ForbiddenPeriod?
)
