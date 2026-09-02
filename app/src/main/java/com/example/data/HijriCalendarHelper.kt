package com.example.data

import java.util.Calendar
import kotlin.math.floor

object HijriCalendarHelper {

    private val islamicMonthsEn = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qi'dah", "Dhu al-Hijjah"
    )

    private val islamicMonthsBn = listOf(
        "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শা\'বান",
        "রমজান", "শাওয়াল", "জিলকদ", "জিলহজ"
    )

    private val islamicMonthsAr = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    data class HijriDate(
        val day: Int,
        val month: Int, // 1..12
        val year: Int,
        val monthNameEn: String,
        val monthNameBn: String,
        val monthNameAr: String
    )

    fun convertGregorianToHijri(calendar: Calendar, offsetDays: Int = 0): HijriDate {
        val cal = (calendar.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, offsetDays)
        }

        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        // Kuwaiti Algorithm / Julian Day computation
        var year = y
        var month = m
        if (month < 3) {
            year -= 1
            month += 12
        }

        val a = floor(year / 100.0)
        val b = 2 - a + floor(a / 4.0)
        val jd = floor(365.25 * (year + 4716)) + floor(30.6001 * (month + 1)) + d + b - 1524

        // Julian Day to Hijri
        val l = jd - 1948440 + 10632
        val n = floor((l - 1) / 10631.0)
        val l2 = l - 10631 * n + 354
        val j = (floor((10985 - l2) / 5316.0)) * (floor((50 * l2) / 17719.0)) + (floor(l2 / 5670.0)) * (floor((43 * l2) / 15238.0))
        val l3 = l2 - (floor((30 - j) / 15.0)) * (floor((17719 * j) / 50.0)) - (floor(j / 16.0)) * (floor((15238 * j) / 43.0)) + 29
        val hMonth = floor((24 * l3) / 709.0).toInt()
        val hDay = (l3 - floor((709 * hMonth) / 24.0)).toInt()
        val hYear = (30 * n + j - 30).toInt()

        val normalizedMonth = (hMonth % 12).coerceIn(1, 12)
        val monthIndex = normalizedMonth - 1

        return HijriDate(
            day = hDay.coerceIn(1, 30),
            month = normalizedMonth,
            year = hYear,
            monthNameEn = islamicMonthsEn.getOrElse(monthIndex) { "Ramadan" },
            monthNameBn = islamicMonthsBn.getOrElse(monthIndex) { "রমজান" },
            monthNameAr = islamicMonthsAr.getOrElse(monthIndex) { "رمضان" }
        )
    }

    fun getHijriDateString(calendar: Calendar, offsetDays: Int = 0): String {
        val hijri = convertGregorianToHijri(calendar, offsetDays)
        return "${hijri.day} ${hijri.monthNameEn} ${hijri.year} AH"
    }

    fun getHijriDateStringBn(calendar: Calendar, offsetDays: Int = 0): String {
        val hijri = convertGregorianToHijri(calendar, offsetDays)
        return "${toBengaliDigits(hijri.day)} ${hijri.monthNameBn} ${toBengaliDigits(hijri.year)} হিজরি"
    }

    private fun toBengaliDigits(num: Int): String {
        val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
        return num.toString().map { if (it in '0'..'9') bnDigits[it - '0'] else it }.joinToString("")
    }
}
