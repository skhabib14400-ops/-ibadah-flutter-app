package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMethod
import com.example.model.Madhab
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onOpenLocationPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.settingsFlow.collectAsState()
    val location by viewModel.locationFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()

    var languageDropdownExpanded by remember { mutableStateOf(false) }
    var madhabDropdownExpanded by remember { mutableStateOf(false) }
    var calcDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = translations.settings,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = PureWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreenPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section: Location & Language
            SettingsSectionHeader(title = "General & Location")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Location item
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLocationPicker() },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = "Location", tint = EmeraldGreenPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = translations.selectLocation, fontWeight = FontWeight.Bold)
                                Text(text = location.displayFormatted, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = "Edit", tint = TextMuted)
                    }

                    HorizontalDivider()

                    // Language Dropdown
                    ExposedDropdownMenuBox(
                        expanded = languageDropdownExpanded,
                        onExpandedChange = { languageDropdownExpanded = !languageDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "${settings.language.nativeName} (${settings.language.displayName})",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(translations.language) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageDropdownExpanded) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = languageDropdownExpanded,
                            onDismissRequest = { languageDropdownExpanded = false }
                        ) {
                            AppLanguage.entries.forEach { lang ->
                                DropdownMenuItem(
                                    text = { Text("${lang.nativeName} - ${lang.displayName}") },
                                    onClick = {
                                        viewModel.setLanguage(lang)
                                        languageDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    // Dark mode toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DarkMode, contentDescription = "Dark Mode", tint = EmeraldGreenPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = translations.theme, fontWeight = FontWeight.SemiBold)
                        }
                        Switch(
                            checked = settings.isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = EmeraldGreenPrimary)
                        )
                    }
                }
            }

            // Section: Prayer & Fiqh Calculations
            SettingsSectionHeader(title = "Prayer Timings & Fiqh")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Madhab Dropdown
                    ExposedDropdownMenuBox(
                        expanded = madhabDropdownExpanded,
                        onExpandedChange = { madhabDropdownExpanded = !madhabDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = if (settings.madhab == Madhab.HANAFI) translations.hanafi else translations.shafi,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(translations.madhab) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = madhabDropdownExpanded) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = madhabDropdownExpanded,
                            onDismissRequest = { madhabDropdownExpanded = false }
                        ) {
                            Madhab.entries.forEach { m ->
                                DropdownMenuItem(
                                    text = { Text(if (m == Madhab.HANAFI) translations.hanafi else translations.shafi) },
                                    onClick = {
                                        viewModel.updateSettings(settings.copy(madhab = m))
                                        madhabDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Calculation Method Dropdown
                    ExposedDropdownMenuBox(
                        expanded = calcDropdownExpanded,
                        onExpandedChange = { calcDropdownExpanded = !calcDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = settings.calculationMethod.title,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(translations.calculationMethod) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = calcDropdownExpanded) },
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = calcDropdownExpanded,
                            onDismissRequest = { calcDropdownExpanded = false }
                        ) {
                            CalculationMethod.entries.forEach { cm ->
                                DropdownMenuItem(
                                    text = { Text(cm.title) },
                                    onClick = {
                                        viewModel.updateSettings(settings.copy(calculationMethod = cm))
                                        calcDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider()

                    // Hijri Date Adjustment Slider
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = translations.hijriAdjustment, fontWeight = FontWeight.SemiBold)
                            Text(
                                text = "${if (settings.hijriOffsetDays > 0) "+" else ""}${settings.hijriOffsetDays} days",
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreenDark
                            )
                        }
                        Slider(
                            value = settings.hijriOffsetDays.toFloat(),
                            onValueChange = {
                                viewModel.updateSettings(settings.copy(hijriOffsetDays = it.roundToInt()))
                            },
                            valueRange = -2f..2f,
                            steps = 3,
                            colors = SliderDefaults.colors(thumbColor = EmeraldGreenPrimary, activeTrackColor = EmeraldGreenPrimary)
                        )
                    }

                    HorizontalDivider()

                    // Athan Notification Sound Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = "Athan Alert", tint = EmeraldGreenPrimary)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Athan Call Audio Alerts", fontWeight = FontWeight.SemiBold)
                        }
                        Switch(
                            checked = settings.athanAlertSound,
                            onCheckedChange = { viewModel.updateSettings(settings.copy(athanAlertSound = it)) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PureWhite, checkedTrackColor = EmeraldGreenPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = EmeraldGreenDark,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}
