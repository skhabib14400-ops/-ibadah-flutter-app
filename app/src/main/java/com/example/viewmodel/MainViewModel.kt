package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.BangladeshLocationsData
import com.example.data.DuaRepository
import com.example.data.HijriCalendarHelper
import com.example.data.NamesOfAllahRepository
import com.example.data.PrayerCalculator
import com.example.data.PreferencesManager
import com.example.data.QuranRepository
import com.example.model.AppSettings
import com.example.model.Ayah
import com.example.model.DailyPrayerSummary
import com.example.model.DuaCategory
import com.example.model.IslamicDua
import com.example.model.Surah
import com.example.model.UserLocation
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.LocalizedStrings
import com.example.ui.localization.TranslationStrings
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MainViewModel(application: Application) : AndroidViewModel(application), SensorEventListener {

    private val prefsManager = PreferencesManager(application)
    val settingsFlow: StateFlow<AppSettings> = prefsManager.settingsFlow
    val locationFlow: StateFlow<UserLocation> = prefsManager.locationFlow

    val tasbihCountFlow: StateFlow<Int> = prefsManager.tasbihCountFlow
    val tasbihTargetFlow: StateFlow<Int> = prefsManager.tasbihTargetFlow
    val tasbihTotalFlow: StateFlow<Int> = prefsManager.tasbihTotalFlow
    val tasbihDhikrIndex: StateFlow<Int> = prefsManager.tasbihDhikrIndex

    private val _currentMillis = MutableStateFlow(System.currentTimeMillis())
    val currentMillis: StateFlow<Long> = _currentMillis.asStateFlow()

    // Compass Sensor State
    private val sensorManager = application.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    private val _deviceHeading = MutableStateFlow(0f)
    val deviceHeading: StateFlow<Float> = _deviceHeading.asStateFlow()

    // UI State for selected tabs/screens & searches
    private val _selectedDuaCategory = MutableStateFlow(DuaCategory.MORNING_EVENING)
    val selectedDuaCategory: StateFlow<DuaCategory> = _selectedDuaCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoriteDuaIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteDuaIds: StateFlow<Set<String>> = _favoriteDuaIds.asStateFlow()

    private val _bookmarkedSurahNumbers = MutableStateFlow<Set<Int>>(emptySet())
    val bookmarkedSurahNumbers: StateFlow<Set<Int>> = _bookmarkedSurahNumbers.asStateFlow()

    // Surah Ayahs dynamic state
    private val _surahAyahsMap = MutableStateFlow<Map<Int, List<Ayah>>>(emptyMap())
    val surahAyahsMap: StateFlow<Map<Int, List<Ayah>>> = _surahAyahsMap.asStateFlow()

    private val _isLoadingAyahs = MutableStateFlow(false)
    val isLoadingAyahs: StateFlow<Boolean> = _isLoadingAyahs.asStateFlow()

    // Audio Playback State
    private val _isPlayingAudio = MutableStateFlow(false)
    val isPlayingAudio: StateFlow<Boolean> = _isPlayingAudio.asStateFlow()

    private val _playingSurahNumber = MutableStateFlow<Int?>(null)
    val playingSurahNumber: StateFlow<Int?> = _playingSurahNumber.asStateFlow()

    private val _playingAyahNumber = MutableStateFlow<Int?>(null)
    val playingAyahNumber: StateFlow<Int?> = _playingAyahNumber.asStateFlow()

    private val _activeAyahIndex = MutableStateFlow(0)
    val activeAyahIndex: StateFlow<Int> = _activeAyahIndex.asStateFlow()

    private val _selectedReciterIndex = MutableStateFlow(0)
    val selectedReciterIndex: StateFlow<Int> = _selectedReciterIndex.asStateFlow()

    private val _isFullSurahMode = MutableStateFlow(true)
    val isFullSurahMode: StateFlow<Boolean> = _isFullSurahMode.asStateFlow()

    // Daily prayer summary reactive state
    val prayerSummaryFlow: StateFlow<DailyPrayerSummary> = combine(
        _currentMillis,
        locationFlow,
        settingsFlow
    ) { nowMillis, loc, settings ->
        PrayerCalculator.calculateDailySummary(nowMillis, loc, settings)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PrayerCalculator.calculateDailySummary(
            System.currentTimeMillis(),
            locationFlow.value,
            settingsFlow.value
        )
    )

    // Current Translations
    val translations: StateFlow<TranslationStrings> = combine(
        settingsFlow
    ) { settings ->
        LocalizedStrings.get(settings[0].language)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = LocalizedStrings.get(settingsFlow.value.language)
    )

    // Qibla Info (Heading and Distance)
    val qiblaAngle: StateFlow<Double> = combine(locationFlow) { loc ->
        calculateQiblaBearing(loc[0].latitude, loc[0].longitude)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = calculateQiblaBearing(locationFlow.value.latitude, locationFlow.value.longitude)
    )

    val distanceToKaabaKm: StateFlow<Int> = combine(locationFlow) { loc ->
        calculateDistanceToKaabaKm(loc[0].latitude, loc[0].longitude)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = calculateDistanceToKaabaKm(locationFlow.value.latitude, locationFlow.value.longitude)
    )

    init {
        // Start 1-second ticker to update countdowns and current time
        viewModelScope.launch {
            while (true) {
                delay(1000L)
                _currentMillis.value = System.currentTimeMillis()
            }
        }
        startCompassSensor()
    }

    private fun startCompassSensor() {
        sensorManager?.let { sm ->
            sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let { acc ->
                sm.registerListener(this, acc, SensorManager.SENSOR_DELAY_UI)
            }
            sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.let { mag ->
                sm.registerListener(this, mag, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        val success = SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )
        if (success) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            val azimuthInRadians = orientationAngles[0]
            val azimuthInDegrees = (Math.toDegrees(azimuthInRadians.toDouble()) + 360.0) % 360.0
            _deviceHeading.value = azimuthInDegrees.toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onCleared() {
        super.onCleared()
        sensorManager?.unregisterListener(this)
    }

    // Setting Updates
    fun updateSettings(newSettings: AppSettings) {
        prefsManager.saveSettings(newSettings)
    }

    fun setLanguage(language: AppLanguage) {
        val updated = settingsFlow.value.copy(language = language)
        prefsManager.saveSettings(updated)
    }

    fun toggleDarkMode(isDark: Boolean) {
        val updated = settingsFlow.value.copy(isDarkMode = isDark)
        prefsManager.saveSettings(updated)
    }

    fun setLocation(newLocation: UserLocation) {
        prefsManager.saveLocation(newLocation)
    }

    fun completeOnboarding() {
        val updated = settingsFlow.value.copy(isOnboardingComplete = true)
        prefsManager.saveSettings(updated)
    }

    // Tasbih Counter Logic
    fun incrementTasbih() {
        val count = tasbihCountFlow.value + 1
        val target = tasbihTargetFlow.value
        val total = tasbihTotalFlow.value + 1
        val dhikrIndex = tasbihDhikrIndex.value

        vibrateClick()
        if (count >= target) {
            // Target reached haptic pulse
            vibrateLong()
            prefsManager.updateTasbih(0, target, total, dhikrIndex)
        } else {
            prefsManager.updateTasbih(count, target, total, dhikrIndex)
        }
    }

    fun resetTasbih() {
        prefsManager.resetTasbih(tasbihTargetFlow.value)
    }

    fun setTasbihTarget(newTarget: Int) {
        prefsManager.updateTasbih(0, newTarget, tasbihTotalFlow.value, tasbihDhikrIndex.value)
    }

    fun setTasbihDhikr(index: Int) {
        prefsManager.updateTasbih(0, tasbihTargetFlow.value, tasbihTotalFlow.value, index)
    }

    private fun vibrateClick() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(20)
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }

    private fun vibrateLong() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = getApplication<Application>().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                getApplication<Application>().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        } catch (e: Exception) {
            // Ignore
        }
    }

    // Dua Favorites
    fun toggleFavoriteDua(duaId: String) {
        val current = _favoriteDuaIds.value.toMutableSet()
        if (current.contains(duaId)) {
            current.remove(duaId)
        } else {
            current.add(duaId)
        }
        _favoriteDuaIds.value = current
    }

    fun setDuaCategory(category: DuaCategory) {
        _selectedDuaCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Quran Audio Playback Methods
    fun playSurah(surahNumber: Int) {
        _playingSurahNumber.value = surahNumber
        _playingAyahNumber.value = null
        _isFullSurahMode.value = true
        _activeAyahIndex.value = 0
        _isPlayingAudio.value = true
    }

    fun playAyah(surahNumber: Int, ayahNumber: Int) {
        _playingSurahNumber.value = surahNumber
        _playingAyahNumber.value = ayahNumber
        _isFullSurahMode.value = false
        _activeAyahIndex.value = (ayahNumber - 1).coerceAtLeast(0)
        _isPlayingAudio.value = true
    }

    fun togglePlayAudio() {
        _isPlayingAudio.value = !_isPlayingAudio.value
    }

    fun stopAudio() {
        _isPlayingAudio.value = false
        _playingSurahNumber.value = null
        _playingAyahNumber.value = null
    }

    fun nextAyah(totalAyahs: Int) {
        val current = _activeAyahIndex.value
        if (current + 1 < totalAyahs) {
            _activeAyahIndex.value = current + 1
            if (_playingAyahNumber.value != null) {
                _playingAyahNumber.value = current + 2
            }
        }
    }

    fun previousAyah() {
        val current = _activeAyahIndex.value
        if (current > 0) {
            _activeAyahIndex.value = current - 1
            if (_playingAyahNumber.value != null) {
                _playingAyahNumber.value = current
            }
        }
    }

    fun setActiveAyah(index: Int) {
        _activeAyahIndex.value = index
    }

    fun setReciterIndex(index: Int) {
        _selectedReciterIndex.value = index
    }

    fun toggleBookmarkSurah(surahNumber: Int) {
        val current = _bookmarkedSurahNumbers.value.toMutableSet()
        if (current.contains(surahNumber)) {
            current.remove(surahNumber)
        } else {
            current.add(surahNumber)
        }
        _bookmarkedSurahNumbers.value = current
    }

    fun loadSurahAyahs(surahNumber: Int) {
        // Pre-fill immediately so user sees all ayahs with zero delay
        if (!_surahAyahsMap.value.containsKey(surahNumber)) {
            val initial = QuranRepository.getAyahsForSurah(surahNumber)
            _surahAyahsMap.value = _surahAyahsMap.value + (surahNumber to initial)
        }

        viewModelScope.launch {
            _isLoadingAyahs.value = true
            try {
                val fullAyahs = QuranRepository.fetchAyahsForSurah(surahNumber)
                if (fullAyahs.isNotEmpty()) {
                    _surahAyahsMap.value = _surahAyahsMap.value + (surahNumber to fullAyahs)
                }
            } catch (e: Exception) {
                // Keep preloaded full ayahs
            } finally {
                _isLoadingAyahs.value = false
            }
        }
    }

    companion object {
        const val KAABA_LAT = 21.4225
        const val KAABA_LNG = 39.8262

        fun calculateQiblaBearing(userLat: Double, userLng: Double): Double {
            val phiK = Math.toRadians(KAABA_LAT)
            val lambdaK = Math.toRadians(KAABA_LNG)
            val phi = Math.toRadians(userLat)
            val lambda = Math.toRadians(userLng)

            val deltaLambda = lambdaK - lambda
            val y = sin(deltaLambda) * cos(phiK)
            val x = cos(phi) * sin(phiK) - sin(phi) * cos(phiK) * cos(deltaLambda)
            val bearingRad = atan2(y, x)
            val bearingDeg = Math.toDegrees(bearingRad)
            return (bearingDeg + 360.0) % 360.0
        }

        fun calculateDistanceToKaabaKm(userLat: Double, userLng: Double): Int {
            val r = 6371.0 // Earth radius in KM
            val dLat = Math.toRadians(KAABA_LAT - userLat)
            val dLng = Math.toRadians(KAABA_LNG - userLng)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(userLat)) * cos(Math.toRadians(KAABA_LAT)) *
                    sin(dLng / 2) * sin(dLng / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return (r * c).toInt()
        }
    }
}
