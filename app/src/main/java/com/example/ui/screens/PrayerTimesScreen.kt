package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrayerType
import com.example.model.SinglePrayerSchedule
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerTimesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.prayerSummaryFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()
    val settings by viewModel.settingsFlow.collectAsState()
    val location by viewModel.locationFlow.collectAsState()

    val notificationStates = remember {
        mutableStateMapOf(
            PrayerType.FAJR to true,
            PrayerType.DHUHR to true,
            PrayerType.ASR to true,
            PrayerType.MAGHRIB to true,
            PrayerType.ISHA to true
        )
    }

    val fardhPrayers = remember(summary) {
        listOf(summary.fajr, summary.dhuhr, summary.asr, summary.maghrib, summary.isha)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = {
                    Column {
                        Text(
                            text = translations.prayerTimes,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                        Text(
                            text = "${summary.hijriDateString} • ${location.displayFormatted}",
                            style = MaterialTheme.typography.bodySmall,
                            color = IslamicGoldLight
                        )
                    }
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
                colors = TopAppBarDefaults.mediumTopAppBarColors(
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Info Card (Sleek card design)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Calculation Settings",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "Madhab: ${if (settings.madhab.name == "HANAFI") translations.hanafi else translations.shafi}",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Method: ${settings.calculationMethod.title}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldGreenLight
                    ) {
                        Text(
                            text = "5 Daily Salah",
                            color = EmeraldGreenDark,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
            }

            Text(
                text = "Fardh Prayers",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )

            // Fardh Prayers List
            fardhPrayers.forEach { prayer ->
                val isNotifOn = notificationStates[prayer.type] ?: true
                PrayerTimeCard(
                    prayer = prayer,
                    isNotificationOn = isNotifOn,
                    onToggleNotification = {
                        notificationStates[prayer.type] = !isNotifOn
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Voluntary & Astronomical Times",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Sunrise, Ishraq, Chasht, Tahajjud
            AuxiliaryPrayerCard(
                title = translations.sunrise,
                arabic = "الشروق",
                time = summary.sunrise.timeFormatted,
                icon = Icons.Default.Brightness5,
                color = IslamicGold
            )
            AuxiliaryPrayerCard(
                title = translations.ishraq,
                arabic = "الإشراق",
                time = summary.ishraq.timeFormatted,
                icon = Icons.Default.WbSunny,
                color = EmeraldGreenPrimary
            )
            AuxiliaryPrayerCard(
                title = translations.chasht,
                arabic = "الضحى",
                time = summary.chasht.timeFormatted,
                icon = Icons.Default.WbSunny,
                color = EmeraldGreenPrimary
            )
            AuxiliaryPrayerCard(
                title = translations.sunset,
                arabic = "الغروب",
                time = summary.sunset.timeFormatted,
                icon = Icons.Default.Brightness6,
                color = WarmSunsetOrange
            )
            AuxiliaryPrayerCard(
                title = translations.tahajjud,
                arabic = "التهجد",
                time = summary.tahajjud.timeFormatted,
                icon = Icons.Default.NightsStay,
                color = EmeraldGreenDark
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrayerTimeCard(
    prayer: SinglePrayerSchedule,
    isNotificationOn: Boolean,
    onToggleNotification: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("prayer_card_${prayer.type.name.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isCurrent) EmeraldGreenLight else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder(),
        elevation = CardDefaults.cardElevation(defaultElevation = if (prayer.isCurrent) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            if (prayer.isCurrent) EmeraldGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getPrayerIcon(prayer.type),
                        contentDescription = prayer.type.displayName,
                        tint = if (prayer.isCurrent) PureWhite else EmeraldGreenPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prayer.type.displayName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (prayer.isCurrent) EmeraldGreenDark else MaterialTheme.colorScheme.onSurface
                        )
                        if (prayer.isCurrent) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldGreenPrimary
                            ) {
                                Text(
                                    text = "ACTIVE",
                                    color = PureWhite,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = prayer.type.arabicName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = prayer.timeFormatted,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (prayer.isCurrent) EmeraldGreenDark else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = onToggleNotification,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isNotificationOn) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                        contentDescription = "Notification",
                        tint = if (isNotificationOn) EmeraldGreenPrimary else TextMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun AuxiliaryPrayerCard(
    title: String,
    arabic: String,
    time: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(color.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = arabic,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            Text(
                text = time,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun getPrayerIcon(type: PrayerType): ImageVector {
    return when (type) {
        PrayerType.FAJR -> Icons.Default.Nightlight
        PrayerType.DHUHR -> Icons.Default.WbSunny
        PrayerType.ASR -> Icons.Default.WbSunny
        PrayerType.MAGHRIB -> Icons.Default.Brightness6
        PrayerType.ISHA -> Icons.Default.NightsStay
        else -> Icons.Default.AccessTime
    }
}
