package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.model.UserLocation
import com.example.ui.components.CurrentPrayerHeroCard
import com.example.ui.components.DailyAyahCard
import com.example.ui.components.FeatureGridSection
import com.example.ui.components.ForbiddenTimesBanner
import com.example.ui.components.LocationPickerBottomSheet
import com.example.ui.components.SahriIftarCard
import com.example.ui.components.TopHeaderSection
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val location by viewModel.locationFlow.collectAsState()
    val summary by viewModel.prayerSummaryFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()
    val settings by viewModel.settingsFlow.collectAsState()

    var showLocationSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Header (Location, Hijri Date, Gregorian Date, Sunrise/Sunset badges)
            TopHeaderSection(
                location = location,
                summary = summary,
                translations = translations,
                onEditLocationClick = { showLocationSheet = true },
                onNotificationsToggle = {
                    viewModel.updateSettings(settings.copy(athanAlertSound = !settings.athanAlertSound))
                },
                isNotificationActive = settings.athanAlertSound
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Current Prayer & Countdown Card
                CurrentPrayerHeroCard(
                    summary = summary,
                    translations = translations,
                    onPrayerTimesClick = { onNavigate("prayer_times") }
                )

                // Sahri & Iftar Card
                SahriIftarCard(
                    summary = summary,
                    translations = translations,
                    onSahriIftarClick = { onNavigate("sahri_iftar") }
                )

                // Forbidden Times Banner
                ForbiddenTimesBanner(
                    summary = summary,
                    translations = translations
                )

                // 3x3 Feature Grid
                FeatureGridSection(
                    translations = translations,
                    onFeatureClick = { featureId ->
                        onNavigate(featureId)
                    }
                )

                // Daily Ayah of the day card
                DailyAyahCard(translations = translations)

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Location Picker Modal Bottom Sheet
        if (showLocationSheet) {
            LocationPickerBottomSheet(
                currentLocation = location,
                translations = translations,
                sheetState = sheetState,
                onDismiss = { showLocationSheet = false },
                onLocationSelected = { newLoc ->
                    viewModel.setLocation(newLoc)
                }
            )
        }
    }
}
