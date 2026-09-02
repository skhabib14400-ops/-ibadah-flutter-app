package com.example.data

import com.example.model.AppSettings
import com.example.model.CalculationMethod
import com.example.model.DailyPrayerSummary
import com.example.model.ForbiddenPeriod
import com.example.model.Madhab
import com.example.model.PrayerType
import com.example.model.SinglePrayerSchedule
import com.example.model.UserLocation
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerCalculator {

    private const val DEG_TO_RAD = Math.PI / 180.0
    private const val RAD_TO_DEG = 180.0 / Math.PI

    data class RawPrayerHours(
        val fajr: Double,
        val sunrise: Double,
        val dhuhr: Double,
        val asr: Double,
        val sunset: Double,
        val maghrib: Double,
        val isha: Double,
        val midnight: Double
    )

    fun getLocationTimeZone(location: UserLocation): TimeZone {
        if (location.countryName.equals("Bangladesh", ignoreCase = true) ||
            location.divisionName != null ||
            (location.latitude in 20.0..27.0 && location.longitude in 88.0..93.0)
        ) {
            return TimeZone.getTimeZone("Asia/Dhaka")
        }
        val offsetHours = Math.round(location.longitude / 15.0).toInt().coerceIn(-12, 14)
        val customId = String.format(Locale.US, "GMT%+03d:00", offsetHours)
        return TimeZone.getTimeZone(customId)
    }

    fun calculateDailySummary(
        currentMillis: Long,
        location: UserLocation,
        settings: AppSettings
    ): DailyPrayerSummary {
        val targetZone = getLocationTimeZone(location)

        val cal = Calendar.getInstance(targetZone).apply {
            timeInMillis = currentMillis
        }

        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val timezoneOffset = targetZone.getOffset(currentMillis) / 3600000.0

        val rawToday = calculateRawHours(year, month, day, location.latitude, location.longitude, timezoneOffset, settings)

        // Tomorrow for Sahri & Iftar tomorrow and Next Fajr
        val calTomorrow = (cal.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, 1) }
        val rawTomorrow = calculateRawHours(
            calTomorrow.get(Calendar.YEAR),
            calTomorrow.get(Calendar.MONTH) + 1,
            calTomorrow.get(Calendar.DAY_OF_MONTH),
            location.latitude,
            location.longitude,
            timezoneOffset,
            settings
        )

        // Yesterday for Isha span if currently before Fajr
        val calYesterday = (cal.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -1) }
        val rawYesterday = calculateRawHours(
            calYesterday.get(Calendar.YEAR),
            calYesterday.get(Calendar.MONTH) + 1,
            calYesterday.get(Calendar.DAY_OF_MONTH),
            location.latitude,
            location.longitude,
            timezoneOffset,
            settings
        )

        // Start of day midnight in target zone
        val startOfDayCal = Calendar.getInstance(targetZone).apply {
            timeInMillis = currentMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDayMillis = startOfDayCal.timeInMillis
        val startOfTomorrowMillis = startOfDayMillis + 24 * 3600000L
        val startOfYesterdayMillis = startOfDayMillis - 24 * 3600000L

        val fajrMillis = startOfDayMillis + hoursToMillis(rawToday.fajr) + settings.fajrOffsetMinutes * 60000L
        val sunriseMillis = startOfDayMillis + hoursToMillis(rawToday.sunrise)
        val ishraqMillis = sunriseMillis + 15 * 60000L
        val chashtMillis = sunriseMillis + 45 * 60000L
        val zawalStartMillis = startOfDayMillis + hoursToMillis(rawToday.dhuhr) - 10 * 60000L
        val dhuhrMillis = startOfDayMillis + hoursToMillis(rawToday.dhuhr) + settings.dhuhrOffsetMinutes * 60000L
        val asrMillis = startOfDayMillis + hoursToMillis(rawToday.asr) + settings.asrOffsetMinutes * 60000L
        val sunsetMillis = startOfDayMillis + hoursToMillis(rawToday.sunset)
        val maghribMillis = startOfDayMillis + hoursToMillis(rawToday.maghrib) + settings.maghribOffsetMinutes * 60000L
        val ishaMillis = startOfDayMillis + hoursToMillis(rawToday.isha) + settings.ishaOffsetMinutes * 60000L
        
        val nightDuration = (fajrMillis + 24 * 3600000L) - maghribMillis
        val tahajjudMillis = maghribMillis + (nightDuration * 2 / 3)

        // Sahri End is ~1 minute before Fajr for safety, Iftar is Maghrib
        val sahriEndMillis = fajrMillis - 1 * 60000L
        val iftarMillis = maghribMillis

        // Tomorrow schedules
        val fajrTomorrowMillis = startOfTomorrowMillis + hoursToMillis(rawTomorrow.fajr) + settings.fajrOffsetMinutes * 60000L
        val sunriseTomorrowMillis = startOfTomorrowMillis + hoursToMillis(rawTomorrow.sunrise)
        val maghribTomorrowMillis = startOfTomorrowMillis + hoursToMillis(rawTomorrow.maghrib) + settings.maghribOffsetMinutes * 60000L
        val sahriEndTomorrowMillis = fajrTomorrowMillis - 1 * 60000L
        val iftarTomorrowMillis = maghribTomorrowMillis

        // Yesterday Isha
        val ishaYesterdayMillis = startOfYesterdayMillis + hoursToMillis(rawYesterday.isha) + settings.ishaOffsetMinutes * 60000L

        val timeFormatter = SimpleDateFormat("hh:mm a", Locale.ENGLISH).apply {
            timeZone = targetZone
        }
        val dateFormatter = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH).apply {
            timeZone = targetZone
        }

        val fajrSchedule = SinglePrayerSchedule(PrayerType.FAJR, fajrMillis, timeFormatter.format(fajrMillis))
        val sunriseSchedule = SinglePrayerSchedule(PrayerType.SUNRISE, sunriseMillis, timeFormatter.format(sunriseMillis))
        val ishraqSchedule = SinglePrayerSchedule(PrayerType.ISHRAQ, ishraqMillis, timeFormatter.format(ishraqMillis))
        val chashtSchedule = SinglePrayerSchedule(PrayerType.CHASHT, chashtMillis, timeFormatter.format(chashtMillis))
        val dhuhrSchedule = SinglePrayerSchedule(PrayerType.DHUHR, dhuhrMillis, timeFormatter.format(dhuhrMillis))
        val asrSchedule = SinglePrayerSchedule(PrayerType.ASR, asrMillis, timeFormatter.format(asrMillis))
        val sunsetSchedule = SinglePrayerSchedule(PrayerType.SUNSET, sunsetMillis, timeFormatter.format(sunsetMillis))
        val maghribSchedule = SinglePrayerSchedule(PrayerType.MAGHRIB, maghribMillis, timeFormatter.format(maghribMillis))
        val ishaSchedule = SinglePrayerSchedule(PrayerType.ISHA, ishaMillis, timeFormatter.format(ishaMillis))
        val tahajjudSchedule = SinglePrayerSchedule(PrayerType.TAHAJJUD, tahajjudMillis, timeFormatter.format(tahajjudMillis))

        val sahriToday = SinglePrayerSchedule(PrayerType.SAHRI_END, sahriEndMillis, timeFormatter.format(sahriEndMillis))
        val iftarToday = SinglePrayerSchedule(PrayerType.IFTAR, iftarMillis, timeFormatter.format(iftarMillis))
        val sahriTomorrow = SinglePrayerSchedule(PrayerType.SAHRI_END, sahriEndTomorrowMillis, timeFormatter.format(sahriEndTomorrowMillis))
        val iftarTomorrow = SinglePrayerSchedule(PrayerType.IFTAR, iftarTomorrowMillis, timeFormatter.format(iftarTomorrowMillis))

        // Dynamic Forbidden Periods
        val forbiddenPeriods = listOf(
            ForbiddenPeriod(
                name = "Sunrise Prohibition",
                arabicName = "عند طلوع الشمس",
                bengaliName = "সূর্যোদয়কালীন নিষিদ্ধ সময়",
                startTimeFormatted = timeFormatter.format(sunriseMillis),
                endTimeFormatted = timeFormatter.format(sunriseMillis + 15 * 60000L),
                startMillis = sunriseMillis,
                endMillis = sunriseMillis + 15 * 60000L,
                description = "From sunrise until 15 minutes after (when the sun rises a spear's height)."
            ),
            ForbiddenPeriod(
                name = "Zawal (Zenith Noon)",
                arabicName = "عند استواء الشمس",
                bengaliName = "দ্বিপ্রহর / যাওয়াল নিষিদ্ধ সময়",
                startTimeFormatted = timeFormatter.format(zawalStartMillis),
                endTimeFormatted = timeFormatter.format(dhuhrMillis),
                startMillis = zawalStartMillis,
                endMillis = dhuhrMillis,
                description = "10 minutes before Dhuhr when the sun is at its exact zenith."
            ),
            ForbiddenPeriod(
                name = "Sunset Prohibition",
                arabicName = "عند غروب الشمس",
                bengaliName = "সূর্যাস্তকালীন নিষিদ্ধ সময়",
                startTimeFormatted = timeFormatter.format(maghribMillis - 15 * 60000L),
                endTimeFormatted = timeFormatter.format(maghribMillis),
                startMillis = maghribMillis - 15 * 60000L,
                endMillis = maghribMillis,
                description = "15 minutes before Maghrib until sunset."
            )
        )

        var isCurrentlyForbidden = false
        var activeForbiddenPeriod: ForbiddenPeriod? = null
        for (fp in forbiddenPeriods) {
            if (currentMillis in fp.startMillis..fp.endMillis) {
                isCurrentlyForbidden = true
                activeForbiddenPeriod = fp
                break
            }
        }

        // Determine Active and Next Prayer
        var currentActive: SinglePrayerSchedule
        var nextUpcoming: SinglePrayerSchedule
        var remainingMillis: Long
        var elapsedFraction: Float

        when {
            currentMillis < fajrMillis -> {
                // Before Fajr (Night / Tahajjud / Isha period)
                currentActive = ishaSchedule
                nextUpcoming = fajrSchedule
                remainingMillis = fajrMillis - currentMillis
                val totalSpan = (fajrMillis - ishaYesterdayMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - ishaYesterdayMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            currentMillis < sunriseMillis -> {
                // Fajr time (from Fajr to Sunrise)
                currentActive = fajrSchedule
                nextUpcoming = SinglePrayerSchedule(PrayerType.SUNRISE, sunriseMillis, timeFormatter.format(sunriseMillis))
                remainingMillis = sunriseMillis - currentMillis
                val totalSpan = (sunriseMillis - fajrMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - fajrMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            currentMillis < dhuhrMillis -> {
                // Morning / Ishraq / Chasht
                currentActive = ishraqSchedule
                nextUpcoming = dhuhrSchedule
                remainingMillis = dhuhrMillis - currentMillis
                val totalSpan = (dhuhrMillis - sunriseMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - sunriseMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            currentMillis < asrMillis -> {
                // Dhuhr time
                currentActive = dhuhrSchedule
                nextUpcoming = asrSchedule
                remainingMillis = asrMillis - currentMillis
                val totalSpan = (asrMillis - dhuhrMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - dhuhrMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            currentMillis < maghribMillis -> {
                // Asr time
                currentActive = asrSchedule
                nextUpcoming = maghribSchedule
                remainingMillis = maghribMillis - currentMillis
                val totalSpan = (maghribMillis - asrMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - asrMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            currentMillis < ishaMillis -> {
                // Maghrib time
                currentActive = maghribSchedule
                nextUpcoming = ishaSchedule
                remainingMillis = ishaMillis - currentMillis
                val totalSpan = (ishaMillis - maghribMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - maghribMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
            else -> {
                // Isha time (After Isha start -> Next is tomorrow's Fajr)
                currentActive = ishaSchedule
                nextUpcoming = SinglePrayerSchedule(PrayerType.FAJR, fajrTomorrowMillis, timeFormatter.format(fajrTomorrowMillis))
                remainingMillis = fajrTomorrowMillis - currentMillis
                val totalSpan = (fajrTomorrowMillis - ishaMillis).coerceAtLeast(1L)
                val elapsed = (currentMillis - ishaMillis).coerceAtLeast(0L)
                elapsedFraction = (elapsed.toFloat() / totalSpan.toFloat()).coerceIn(0f, 1f)
            }
        }

        // Sahri & Iftar status
        val isFastingHours = currentMillis in sahriEndMillis..iftarMillis
        val remainingToSahri = if (currentMillis < sahriEndMillis) {
            sahriEndMillis - currentMillis
        } else {
            sahriEndTomorrowMillis - currentMillis
        }
        val remainingToIftar = if (currentMillis < iftarMillis) {
            iftarMillis - currentMillis
        } else {
            iftarTomorrowMillis - currentMillis
        }

        val hijriDateStr = HijriCalendarHelper.getHijriDateString(cal, settings.hijriOffsetDays)

        return DailyPrayerSummary(
            dateString = dateFormatter.format(cal.time),
            hijriDateString = hijriDateStr,
            fajr = fajrSchedule.copy(isCurrent = currentActive.type == PrayerType.FAJR, isNext = nextUpcoming.type == PrayerType.FAJR),
            sunrise = sunriseSchedule,
            ishraq = ishraqSchedule,
            chasht = chashtSchedule,
            zawalStart = timeFormatter.format(zawalStartMillis),
            zawalEnd = timeFormatter.format(dhuhrMillis),
            dhuhr = dhuhrSchedule.copy(isCurrent = currentActive.type == PrayerType.DHUHR, isNext = nextUpcoming.type == PrayerType.DHUHR),
            asr = asrSchedule.copy(isCurrent = currentActive.type == PrayerType.ASR, isNext = nextUpcoming.type == PrayerType.ASR),
            sunset = sunsetSchedule,
            maghrib = maghribSchedule.copy(isCurrent = currentActive.type == PrayerType.MAGHRIB, isNext = nextUpcoming.type == PrayerType.MAGHRIB),
            isha = ishaSchedule.copy(isCurrent = currentActive.type == PrayerType.ISHA, isNext = nextUpcoming.type == PrayerType.ISHA),
            tahajjud = tahajjudSchedule,
            sahriEndToday = sahriToday,
            iftarToday = iftarToday,
            sahriEndTomorrow = sahriTomorrow,
            iftarTomorrow = iftarTomorrow,
            forbiddenPeriods = forbiddenPeriods,
            currentActivePrayer = currentActive,
            nextUpcomingPrayer = nextUpcoming,
            remainingMillisToNextPrayer = remainingMillis.coerceAtLeast(0L),
            elapsedFractionCurrentPrayer = elapsedFraction,
            remainingMillisToSahri = remainingToSahri.coerceAtLeast(0L),
            remainingMillisToIftar = remainingToIftar.coerceAtLeast(0L),
            isFastingHours = isFastingHours,
            isCurrentlyForbidden = isCurrentlyForbidden,
            currentForbiddenPeriod = activeForbiddenPeriod
        )
    }

    private fun hoursToMillis(hours: Double): Long {
        val totalSeconds = (hours * 3600).toLong()
        return totalSeconds * 1000L
    }

    private fun calculateRawHours(
        year: Int,
        month: Int,
        day: Int,
        latitude: Double,
        longitude: Double,
        timezone: Double,
        settings: AppSettings
    ): RawPrayerHours {
        val julianDay = getJulianDay(year, month, day) - longitude / (15.0 * 24.0)

        val d = julianDay - 2451545.0
        val g = fixAngle(357.529 + 0.98560028 * d)
        val q = fixAngle(280.459 + 0.98564736 * d)
        val l = fixAngle(q + 1.915 * sin(g * DEG_TO_RAD) + 0.020 * sin(2 * g * DEG_TO_RAD))

        val e = 23.439 - 0.00000036 * d
        val ra = fixAngle(atan2(cos(e * DEG_TO_RAD) * sin(l * DEG_TO_RAD), cos(l * DEG_TO_RAD)) * RAD_TO_DEG) / 15.0

        val declination = asin(sin(e * DEG_TO_RAD) * sin(l * DEG_TO_RAD)) * RAD_TO_DEG
        val eqOfTime = q / 15.0 - ra

        // Solar Noon (Dhuhr)
        val solarNoon = fixHour(12.0 + timezone - longitude / 15.0 - eqOfTime)

        // Sunrise & Sunset angle (0.833° for refraction)
        val sunAngle = 0.8333
        val sunriseHourAngle = calculateHourAngle(-sunAngle, latitude, declination)
        val sunrise = solarNoon - sunriseHourAngle / 15.0
        val sunset = solarNoon + sunriseHourAngle / 15.0

        // Fajr & Isha based on calculation method
        val fajrAngle = settings.calculationMethod.fajrAngle
        val ishaAngle = settings.calculationMethod.ishaAngle

        val fajrHourAngle = calculateHourAngle(-fajrAngle, latitude, declination)
        val ishaHourAngle = calculateHourAngle(-ishaAngle, latitude, declination)

        val fajr = solarNoon - fajrHourAngle / 15.0
        val isha = solarNoon + ishaHourAngle / 15.0

        // Asr based on Madhab (1.0 for Shafi/Hanbali/Maliki, 2.0 for Hanafi)
        val shadowFactor = settings.madhab.shadowFactor
        val asrAngle = -atan(1.0 / (shadowFactor + tan(abs(latitude - declination) * DEG_TO_RAD))) * RAD_TO_DEG
        val asrHourAngle = calculateHourAngle(asrAngle, latitude, declination)
        val asr = solarNoon + asrHourAngle / 15.0

        val maghrib = sunset
        val midnight = solarNoon + 12.0

        return RawPrayerHours(
            fajr = fixHour(fajr),
            sunrise = fixHour(sunrise),
            dhuhr = fixHour(solarNoon + (1.5 / 60.0)), // standard 1.5 min buffer after solar noon
            asr = fixHour(asr),
            sunset = fixHour(sunset),
            maghrib = fixHour(maghrib),
            isha = fixHour(isha),
            midnight = fixHour(midnight)
        )
    }

    private fun calculateHourAngle(alpha: Double, latitude: Double, declination: Double): Double {
        val cosHA = (sin(alpha * DEG_TO_RAD) - sin(latitude * DEG_TO_RAD) * sin(declination * DEG_TO_RAD)) /
                (cos(latitude * DEG_TO_RAD) * cos(declination * DEG_TO_RAD))
        val clamped = cosHA.coerceIn(-1.0, 1.0)
        return acos(clamped) * RAD_TO_DEG
    }

    private fun getJulianDay(year: Int, month: Int, day: Int): Double {
        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2 - a + floor(a / 4.0)
        return floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5
    }

    private fun fixAngle(angle: Double): Double {
        var a = angle - 360.0 * floor(angle / 360.0)
        if (a < 0) a += 360.0
        return a
    }

    private fun fixHour(hour: Double): Double {
        var h = hour - 24.0 * floor(hour / 24.0)
        if (h < 0) h += 24.0
        return h
    }
}
