package com.example.model

import com.example.ui.localization.AppLanguage

enum class Madhab(val displayName: String, val shadowFactor: Double) {
    HANAFI("Hanafi (2x Shadow)", 2.0),
    SHAFI("Shafi'i / Maliki / Hanbali (1x Shadow)", 1.0)
}

enum class CalculationMethod(
    val title: String,
    val fajrAngle: Double,
    val ishaAngle: Double,
    val description: String
) {
    KARACHI("University of Islamic Sciences, Karachi", 18.0, 18.0, "Used in Pakistan, Bangladesh, India, Afghanistan"),
    ISNA("Islamic Society of North America (ISNA)", 15.0, 15.0, "Used in USA, Canada, North America"),
    MWL("Muslim World League (MWL)", 18.0, 17.0, "Used in Europe, Far East, parts of USA"),
    UMM_AL_QURA("Umm al-Qura University, Makkah", 18.5, 19.0, "Used in Saudi Arabia, Arabian Peninsula"),
    EGYPTIAN("Egyptian General Authority of Survey", 19.5, 17.5, "Used in Egypt, Africa, Middle East")
}

data class AppSettings(
    val language: AppLanguage = AppLanguage.ENGLISH,
    val isDarkMode: Boolean = false,
    val madhab: Madhab = Madhab.HANAFI,
    val calculationMethod: CalculationMethod = CalculationMethod.KARACHI,
    val hijriOffsetDays: Int = 0,
    val fajrOffsetMinutes: Int = 0,
    val dhuhrOffsetMinutes: Int = 0,
    val asrOffsetMinutes: Int = 0,
    val maghribOffsetMinutes: Int = 0,
    val ishaOffsetMinutes: Int = 0,
    val athanAlertSound: Boolean = true,
    val isOnboardingComplete: Boolean = false
)
