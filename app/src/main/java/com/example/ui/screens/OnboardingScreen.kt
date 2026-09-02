package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.BangladeshLocationsData
import com.example.model.AppSettings
import com.example.model.CalculationMethod
import com.example.model.Madhab
import com.example.model.UserLocation
import com.example.ui.components.BangladeshCascadingSelector
import com.example.ui.components.GlobalCitiesSelector
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.LocalizedStrings
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    currentSettings: AppSettings,
    currentLocation: UserLocation,
    onFinishOnboarding: (AppSettings, UserLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by remember { mutableIntStateOf(1) } // 1: Language, 2: Location, 3: Madhab & Calculation

    var selectedLanguage by remember { mutableStateOf(currentSettings.language) }
    var selectedLocation by remember { mutableStateOf(currentLocation) }
    var selectedMadhab by remember { mutableStateOf(currentSettings.madhab) }
    var selectedCalculationMethod by remember { mutableStateOf(currentSettings.calculationMethod) }

    val translations = LocalizedStrings.get(selectedLanguage)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // App Brand Logo & Title
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        Brush.radialGradient(listOf(EmeraldGreenPrimary, EmeraldGreenDark)),
                        shape = RoundedCornerShape(22.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mosque,
                    contentDescription = "Logo",
                    tint = PureWhite,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = translations.appName,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = EmeraldGreenPrimary
            )
            Text(
                text = "Comprehensive Islamic Lifestyle & Prayer Times",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Step Indicator (1 - 2 - 3)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (step in 1..3) {
                    val isActive = step == currentStep
                    val isCompleted = step < currentStep
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isActive || isCompleted) EmeraldGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done",
                                tint = PureWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Text(
                                text = "$step",
                                color = if (isActive) PureWhite else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                    if (step < 3) {
                        Box(
                            modifier = Modifier
                                .width(36.dp)
                                .height(3.dp)
                                .background(
                                    if (step < currentStep) EmeraldGreenPrimary else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Step Content
            when (currentStep) {
                1 -> {
                    // Step 1: Language Selection
                    LanguageStepContent(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it },
                        translations = translations
                    )
                }
                2 -> {
                    // Step 2: Location Setup
                    LocationStepContent(
                        selectedLocation = selectedLocation,
                        onLocationSelected = { selectedLocation = it },
                        translations = translations
                    )
                }
                3 -> {
                    // Step 3: Madhab & Calculation Method
                    MadhabCalculationStepContent(
                        selectedMadhab = selectedMadhab,
                        onMadhabSelected = { selectedMadhab = it },
                        selectedCalculationMethod = selectedCalculationMethod,
                        onCalculationMethodSelected = { selectedCalculationMethod = it },
                        translations = translations
                    )
                }
            }
        }

        // Bottom Controls
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (currentStep > 1) {
                    OutlinedButton(
                        onClick = { currentStep -= 1 },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Back")
                    }
                }

                Button(
                    onClick = {
                        if (currentStep < 3) {
                            currentStep += 1
                        } else {
                            val finalSettings = currentSettings.copy(
                                language = selectedLanguage,
                                madhab = selectedMadhab,
                                calculationMethod = selectedCalculationMethod,
                                isOnboardingComplete = true
                            )
                            onFinishOnboarding(finalSettings, selectedLocation)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("onboarding_next_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenPrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = if (currentStep < 3) translations.continueText else translations.getStarted,
                        color = PureWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageStepContent(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    translations: TranslationStrings
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Language",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Choose your preferred interface language",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        AppLanguage.entries.forEach { lang ->
            val isSelected = lang == selectedLanguage
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onLanguageSelected(lang) }
                    .testTag("lang_choice_${lang.code}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) EmeraldGreenLight else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) {
                    CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, EmeraldGreenPrimary)))
                } else {
                    CardDefaults.outlinedCardBorder()
                },
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = lang.nativeName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) EmeraldGreenDark else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${lang.displayName})",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Selected",
                            tint = EmeraldGreenPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationStepContent(
    selectedLocation: UserLocation,
    onLocationSelected: (UserLocation) -> Unit,
    translations: TranslationStrings
) {
    var locationMode by remember { mutableIntStateOf(0) } // 0: GPS / Cascading BD, 1: Global

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = translations.selectLocation,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Accurate location ensures precise prayer & fasting timings",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Auto GPS Button
        Button(
            onClick = {
                val gpsLoc = UserLocation(
                    cityName = "Dhaka",
                    countryName = "Bangladesh",
                    divisionName = "Dhaka",
                    districtName = "Dhaka",
                    upazilaName = "Dhanmondi",
                    latitude = 23.8103,
                    longitude = 90.4125,
                    isGpsAuto = true
                )
                onLocationSelected(gpsLoc)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.GpsFixed, contentDescription = "GPS", tint = PureWhite)
            Spacer(modifier = Modifier.width(8.dp))
            Text(translations.useGps, color = PureWhite, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Or Select Bangladesh Location:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        BangladeshCascadingSelector(
            currentLocation = selectedLocation,
            translations = translations,
            onSave = { loc: UserLocation -> onLocationSelected(loc) }
        )
    }
}

@Composable
private fun MadhabCalculationStepContent(
    selectedMadhab: Madhab,
    onMadhabSelected: (Madhab) -> Unit,
    selectedCalculationMethod: CalculationMethod,
    onCalculationMethodSelected: (CalculationMethod) -> Unit,
    translations: TranslationStrings
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = translations.madhab,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Affects Asr prayer calculation time",
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Madhab.entries.forEach { m ->
            val isSelected = m == selectedMadhab
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onMadhabSelected(m) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) EmeraldGreenLight else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) {
                    CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, EmeraldGreenPrimary)))
                } else CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (m == Madhab.HANAFI) translations.hanafi else translations.shafi,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) EmeraldGreenDark else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (m == Madhab.HANAFI) "Shadow length reaches 2x object size" else "Shadow length reaches 1x object size",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = EmeraldGreenPrimary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = translations.calculationMethod,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        CalculationMethod.entries.forEach { cm ->
            val isSelected = cm == selectedCalculationMethod
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onCalculationMethodSelected(cm) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) EmeraldGreenLight else MaterialTheme.colorScheme.surface
                ),
                border = if (isSelected) {
                    CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, EmeraldGreenPrimary)))
                } else CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = cm.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) EmeraldGreenDark else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = cm.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    if (isSelected) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = EmeraldGreenPrimary)
                    }
                }
            }
        }
    }
}
