package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppSettings
import com.example.model.CalculationMethod
import com.example.model.Madhab
import com.example.model.UserLocation
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("ibadah_islamic_prefs", Context.MODE_PRIVATE)

    private val _settingsFlow = MutableStateFlow(loadSettings())
    val settingsFlow: StateFlow<AppSettings> = _settingsFlow.asStateFlow()

    private val _locationFlow = MutableStateFlow(loadLocation())
    val locationFlow: StateFlow<UserLocation> = _locationFlow.asStateFlow()

    private val _tasbihCountFlow = MutableStateFlow(prefs.getInt(KEY_TASBIH_COUNT, 0))
    val tasbihCountFlow: StateFlow<Int> = _tasbihCountFlow.asStateFlow()

    private val _tasbihTargetFlow = MutableStateFlow(prefs.getInt(KEY_TASBIH_TARGET, 33))
    val tasbihTargetFlow: StateFlow<Int> = _tasbihTargetFlow.asStateFlow()

    private val _tasbihTotalFlow = MutableStateFlow(prefs.getInt(KEY_TASBIH_TOTAL, 0))
    val tasbihTotalFlow: StateFlow<Int> = _tasbihTotalFlow.asStateFlow()

    private val _tasbihDhikrIndex = MutableStateFlow(prefs.getInt(KEY_TASBIH_DHIKR, 0))
    val tasbihDhikrIndex: StateFlow<Int> = _tasbihDhikrIndex.asStateFlow()

    private fun loadSettings(): AppSettings {
        val langCode = prefs.getString(KEY_LANGUAGE, AppLanguage.ENGLISH.code) ?: AppLanguage.ENGLISH.code
        val isDark = prefs.getBoolean(KEY_DARK_MODE, false)
        val madhabName = prefs.getString(KEY_MADHAB, Madhab.HANAFI.name) ?: Madhab.HANAFI.name
        val calcName = prefs.getString(KEY_CALC_METHOD, CalculationMethod.KARACHI.name) ?: CalculationMethod.KARACHI.name
        val hijriOffset = prefs.getInt(KEY_HIJRI_OFFSET, 0)
        val fajrOff = prefs.getInt(KEY_FAJR_OFFSET, 0)
        val dhuhrOff = prefs.getInt(KEY_DHUHR_OFFSET, 0)
        val asrOff = prefs.getInt(KEY_ASR_OFFSET, 0)
        val maghribOff = prefs.getInt(KEY_MAGHRIB_OFFSET, 0)
        val ishaOff = prefs.getInt(KEY_ISHA_OFFSET, 0)
        val athanSound = prefs.getBoolean(KEY_ATHAN_SOUND, true)
        val onboardingComplete = prefs.getBoolean(KEY_ONBOARDING_DONE, false)

        return AppSettings(
            language = AppLanguage.fromCode(langCode),
            isDarkMode = isDark,
            madhab = try { Madhab.valueOf(madhabName) } catch (e: Exception) { Madhab.HANAFI },
            calculationMethod = try { CalculationMethod.valueOf(calcName) } catch (e: Exception) { CalculationMethod.KARACHI },
            hijriOffsetDays = hijriOffset,
            fajrOffsetMinutes = fajrOff,
            dhuhrOffsetMinutes = dhuhrOff,
            asrOffsetMinutes = asrOff,
            maghribOffsetMinutes = maghribOff,
            ishaOffsetMinutes = ishaOff,
            athanAlertSound = athanSound,
            isOnboardingComplete = onboardingComplete
        )
    }

    fun saveSettings(settings: AppSettings) {
        prefs.edit()
            .putString(KEY_LANGUAGE, settings.language.code)
            .putBoolean(KEY_DARK_MODE, settings.isDarkMode)
            .putString(KEY_MADHAB, settings.madhab.name)
            .putString(KEY_CALC_METHOD, settings.calculationMethod.name)
            .putInt(KEY_HIJRI_OFFSET, settings.hijriOffsetDays)
            .putInt(KEY_FAJR_OFFSET, settings.fajrOffsetMinutes)
            .putInt(KEY_DHUHR_OFFSET, settings.dhuhrOffsetMinutes)
            .putInt(KEY_ASR_OFFSET, settings.asrOffsetMinutes)
            .putInt(KEY_MAGHRIB_OFFSET, settings.maghribOffsetMinutes)
            .putInt(KEY_ISHA_OFFSET, settings.ishaOffsetMinutes)
            .putBoolean(KEY_ATHAN_SOUND, settings.athanAlertSound)
            .putBoolean(KEY_ONBOARDING_DONE, settings.isOnboardingComplete)
            .apply()
        _settingsFlow.value = settings
    }

    private fun loadLocation(): UserLocation {
        val city = prefs.getString(KEY_CITY, "Dhaka") ?: "Dhaka"
        val country = prefs.getString(KEY_COUNTRY, "Bangladesh") ?: "Bangladesh"
        val division = prefs.getString(KEY_DIVISION, "Dhaka")
        val district = prefs.getString(KEY_DISTRICT, "Dhaka")
        val upazila = prefs.getString(KEY_UPAZILA, "Dhanmondi")
        val lat = prefs.getFloat(KEY_LAT, 23.8103f).toDouble()
        val lng = prefs.getFloat(KEY_LNG, 90.4125f).toDouble()
        val isGps = prefs.getBoolean(KEY_IS_GPS, false)

        return UserLocation(
            cityName = city,
            countryName = country,
            divisionName = division,
            districtName = district,
            upazilaName = upazila,
            latitude = lat,
            longitude = lng,
            isGpsAuto = isGps
        )
    }

    fun saveLocation(location: UserLocation) {
        prefs.edit()
            .putString(KEY_CITY, location.cityName)
            .putString(KEY_COUNTRY, location.countryName)
            .putString(KEY_DIVISION, location.divisionName)
            .putString(KEY_DISTRICT, location.districtName)
            .putString(KEY_UPAZILA, location.upazilaName)
            .putFloat(KEY_LAT, location.latitude.toFloat())
            .putFloat(KEY_LNG, location.longitude.toFloat())
            .putBoolean(KEY_IS_GPS, location.isGpsAuto)
            .apply()
        _locationFlow.value = location
    }

    fun updateTasbih(count: Int, target: Int, total: Int, dhikrIndex: Int) {
        prefs.edit()
            .putInt(KEY_TASBIH_COUNT, count)
            .putInt(KEY_TASBIH_TARGET, target)
            .putInt(KEY_TASBIH_TOTAL, total)
            .putInt(KEY_TASBIH_DHIKR, dhikrIndex)
            .apply()
        _tasbihCountFlow.value = count
        _tasbihTargetFlow.value = target
        _tasbihTotalFlow.value = total
        _tasbihDhikrIndex.value = dhikrIndex
    }

    fun resetTasbih(target: Int = 33) {
        prefs.edit()
            .putInt(KEY_TASBIH_COUNT, 0)
            .putInt(KEY_TASBIH_TARGET, target)
            .apply()
        _tasbihCountFlow.value = 0
        _tasbihTargetFlow.value = target
    }

    companion object {
        private const val KEY_LANGUAGE = "key_language"
        private const val KEY_DARK_MODE = "key_dark_mode"
        private const val KEY_MADHAB = "key_madhab"
        private const val KEY_CALC_METHOD = "key_calc_method"
        private const val KEY_HIJRI_OFFSET = "key_hijri_offset"
        private const val KEY_FAJR_OFFSET = "key_fajr_offset"
        private const val KEY_DHUHR_OFFSET = "key_dhuhr_offset"
        private const val KEY_ASR_OFFSET = "key_asr_offset"
        private const val KEY_MAGHRIB_OFFSET = "key_maghrib_offset"
        private const val KEY_ISHA_OFFSET = "key_isha_offset"
        private const val KEY_ATHAN_SOUND = "key_athan_sound"
        private const val KEY_ONBOARDING_DONE = "key_onboarding_done"

        private const val KEY_CITY = "key_city"
        private const val KEY_COUNTRY = "key_country"
        private const val KEY_DIVISION = "key_division"
        private const val KEY_DISTRICT = "key_district"
        private const val KEY_UPAZILA = "key_upazila"
        private const val KEY_LAT = "key_lat"
        private const val KEY_LNG = "key_lng"
        private const val KEY_IS_GPS = "key_is_gps"

        private const val KEY_TASBIH_COUNT = "key_tasbih_count"
        private const val KEY_TASBIH_TARGET = "key_tasbih_target"
        private const val KEY_TASBIH_TOTAL = "key_tasbih_total"
        private const val KEY_TASBIH_DHIKR = "key_tasbih_dhikr"
    }
}
