package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.OndemandVideo
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BangladeshLocationsData
import com.example.model.DailyPrayerSummary
import com.example.model.ForbiddenPeriod
import com.example.model.PrayerType
import com.example.model.UserLocation
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.*

@Composable
fun TopHeaderSection(
    location: UserLocation,
    summary: DailyPrayerSummary,
    translations: TranslationStrings,
    onEditLocationClick: () -> Unit,
    onNotificationsToggle: () -> Unit,
    isNotificationActive: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("top_header_card"),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            // First Row: Location + Subtitle & Notification Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onEditLocationClick() }
                        .testTag("location_selector_pill")
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(EmeraldGreenPrimary.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = EmeraldGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = location.displayFormatted,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.EditLocation,
                                contentDescription = "Edit Location",
                                tint = Slate400,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "Hanafi • Karachi Method",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = Slate500,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                IconButton(
                    onClick = onNotificationsToggle,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Slate100, CircleShape)
                        .testTag("header_notifications_toggle")
                ) {
                    Icon(
                        imageVector = if (isNotificationActive) Icons.Default.NotificationsActive else Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = if (isNotificationActive) EmeraldGreenPrimary else Slate600,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Second Row: Hijri Date, Gregorian Date & Sunrise/Sunset
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = summary.hijriDateString,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = summary.dateString,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate500
                    )
                }

                // Sunrise & Sunset Info
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = translations.sunrise.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400
                        )
                        Text(
                            text = summary.sunrise.timeFormatted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = translations.sunset.uppercase(),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Slate400
                        )
                        Text(
                            text = summary.sunset.timeFormatted,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrentPrayerHeroCard(
    summary: DailyPrayerSummary,
    translations: TranslationStrings,
    onPrayerTimesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activePrayer = summary.currentActivePrayer
    val nextPrayer = summary.nextUpcomingPrayer

    val remainingMillis = summary.remainingMillisToNextPrayer
    val hours = remainingMillis / 3600000L
    val minutes = (remainingMillis % 3600000L) / 60000L
    val seconds = (remainingMillis % 60000L) / 1000L
    val countdownFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onPrayerTimesClick() }
            .testTag("current_prayer_hero_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = EmeraldGreenPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Row: "Now Praying" label + "Next: Maghrib" glassmorphic pill
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = translations.currentPrayer,
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite.copy(alpha = 0.85f),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = activePrayer?.type?.displayName ?: "Asr",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    }

                    if (nextPrayer != null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = PureWhite.copy(alpha = 0.22f)
                        ) {
                            Text(
                                text = "Next: ${nextPrayer.type.displayName}",
                                color = PureWhite,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Countdown Row: Tabular Big Numbers + "Remaining"
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = countdownFormatted,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = PureWhite
                    )
                    Text(
                        text = translations.remainingTime,
                        style = MaterialTheme.typography.labelLarge,
                        color = PureWhite.copy(alpha = 0.85f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { summary.elapsedFractionCurrentPrayer.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = PureWhite,
                    trackColor = PureWhite.copy(alpha = 0.25f)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Starts & Ends Times
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Starts ${activePrayer?.timeFormatted ?: "04:12 PM"}",
                        fontSize = 11.sp,
                        color = PureWhite.copy(alpha = 0.9f)
                    )
                    Text(
                        text = "Ends ${nextPrayer?.timeFormatted ?: "06:14 PM"}",
                        fontSize = 11.sp,
                        color = PureWhite.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun SahriIftarCard(
    summary: DailyPrayerSummary,
    translations: TranslationStrings,
    onSahriIftarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSahriIftarClick() }
            .testTag("sahri_iftar_card"),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sahri Ends Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(OrangeSahriBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NightsStay,
                        contentDescription = "Sahri",
                        tint = WarmSunsetOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = translations.todaySahri.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = summary.sahriEndToday.timeFormatted,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Iftar Starts Card
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(EmeraldIftarBg, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Brightness6,
                        contentDescription = "Iftar",
                        tint = EmeraldIftarText,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = translations.todayIftar.uppercase(),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Slate400,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = summary.iftarToday.timeFormatted,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ForbiddenTimesBanner(
    summary: DailyPrayerSummary,
    translations: TranslationStrings,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val nowMillis = System.currentTimeMillis()
    val activeForbidden = summary.currentForbiddenPeriod
    val nextForbidden = summary.forbiddenPeriods.firstOrNull { it.startMillis > nowMillis }
        ?: summary.forbiddenPeriods.firstOrNull()

    val isForbiddenNow = summary.isCurrentlyForbidden && activeForbidden != null

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("forbidden_times_banner"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isForbiddenNow) ForbiddenRedContainer else MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(
                if (isForbiddenNow) listOf(ForbiddenRedBorder, ForbiddenRedBorder)
                else listOf(IslamicGold.copy(alpha = 0.4f), IslamicGold.copy(alpha = 0.2f))
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .alpha(if (isForbiddenNow) alphaAnim else 1f)
                    .background(if (isForbiddenNow) ForbiddenRed else IslamicGold, CircleShape)
            )

            val text = if (isForbiddenNow && activeForbidden != null) {
                "Prohibited Now: ${activeForbidden.startTimeFormatted} - ${activeForbidden.endTimeFormatted} (${activeForbidden.name})"
            } else if (nextForbidden != null) {
                "Forbidden Times: ${nextForbidden.name} (${nextForbidden.startTimeFormatted} - ${nextForbidden.endTimeFormatted})"
            } else {
                "Forbidden Times (Zawal, Sunrise, Sunset)"
            }

            Text(
                text = text.uppercase(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isForbiddenNow) ForbiddenRed else MaterialTheme.colorScheme.onSurface,
                letterSpacing = 0.2.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

data class FeatureGridItem(
    val id: String,
    val title: String,
    val emoji: String,
    val icon: ImageVector
)

@Composable
fun FeatureGridSection(
    translations: TranslationStrings,
    onFeatureClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        FeatureGridItem("quran", translations.quran, "📖", Icons.Default.MenuBook),
        FeatureGridItem("qibla", translations.qibla, "🧭", Icons.Default.Explore),
        FeatureGridItem("tasbih", translations.tasbih, "📿", Icons.Default.TouchApp),
        FeatureGridItem("dua_dhikr", translations.duaDhikr, "🤲", Icons.Default.Mosque),
        FeatureGridItem("prayer_times", translations.prayerTimes, "🕌", Icons.Default.AccessTime),
        FeatureGridItem("asma_ul_husna", translations.asmaUlHusna, "🌟", Icons.Default.FormatListNumbered),
        FeatureGridItem("sahri_iftar", translations.sahriIftar, "🌙", Icons.Default.Restaurant),
        FeatureGridItem("islamic_videos", translations.islamicVideos, "📺", Icons.Default.PlayCircle)
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // 4 Columns x 2 Rows Grid
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (row in 0 until 2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (col in 0 until 4) {
                        val index = row * 4 + col
                        if (index < items.size) {
                            val item = items[index]
                            SleekFeatureItem(
                                item = item,
                                onClick = { onFeatureClick(item.id) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SleekFeatureItem(
    item: FeatureGridItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .testTag("feature_btn_${item.id}"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            border = CardDefaults.outlinedCardBorder()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.emoji,
                    fontSize = 26.sp,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }

        Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Slate600,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DailyAyahCard(
    translations: TranslationStrings,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("daily_ayah_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Quran",
                        tint = EmeraldGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = translations.dailyAyah,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenDark
                    )
                }

                Text(
                    text = "Surah Al-Baqarah (2:152)",
                    style = MaterialTheme.typography.labelSmall,
                    color = Slate500
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "فَاذْكُرُونِي أَذْكُرْكُمْ وَاشْكُرُوا لِي وَلَا تَكْفُرُونِ",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = EmeraldGreenPrimary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"So remember Me; I will remember you. And be grateful to Me and do not deny Me.\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
        }
    }
}

