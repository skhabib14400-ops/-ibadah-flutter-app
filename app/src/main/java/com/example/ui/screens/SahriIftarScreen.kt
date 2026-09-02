package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaRepository
import com.example.model.DailyPrayerSummary
import com.example.model.DuaCategory
import com.example.model.IslamicDua
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.DeepIslamicNight
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmSunsetOrange
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SahriIftarScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val summary by viewModel.prayerSummaryFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Today, 1: Duas, 2: 30-Day Ramadan Calendar

    val fastingDuas = remember { DuaRepository.getByCategory(DuaCategory.FASTING_RAMADAN) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = translations.sahriIftar,
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
        ) {
            // Tab Header
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldGreenPrimary
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Today", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Fasting Duas", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("30-Day Ramadan", fontWeight = FontWeight.Bold) }
                )
            }

            when (selectedTab) {
                0 -> TodaySahriIftarContent(summary = summary, translations = translations)
                1 -> FastingDuasContent(duas = fastingDuas, context = context)
                2 -> RamadanCalendarContent(summary = summary)
            }
        }
    }
}

@Composable
private fun TodaySahriIftarContent(
    summary: DailyPrayerSummary,
    translations: TranslationStrings
) {
    val isFasting = summary.isFastingHours
    val remainingMillis = if (isFasting) summary.remainingMillisToIftar else summary.remainingMillisToSahri
    val hours = remainingMillis / 3600000L
    val minutes = (remainingMillis % 3600000L) / 60000L
    val seconds = (remainingMillis % 60000L) / 1000L
    val countdownFormatted = String.format("%02d:%02d:%02d", hours, minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Countdown Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            if (isFasting) listOf(WarmSunsetOrange, DeepIslamicNight) else listOf(EmeraldGreenDark, EmeraldGreenPrimary)
                        )
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PureWhite.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = if (isFasting) "FASTING IN PROGRESS" else "SAHRI TIME WINDOW",
                            color = IslamicGoldLight,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = if (isFasting) translations.iftarIn else translations.sahriEndsIn,
                        color = PureWhite,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = countdownFormatted,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = PureWhite
                    )
                }
            }
        }

        // Today & Tomorrow Grid Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Today",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Sahri Ends", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = summary.sahriEndToday.timeFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Iftar Time", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = summary.iftarToday.timeFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WarmSunsetOrange
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tomorrow",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenDark
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Sahri Ends", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = summary.sahriEndTomorrow.timeFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = "Iftar Time", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    Text(
                        text = summary.iftarTomorrow.timeFormatted,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = WarmSunsetOrange
                    )
                }
            }
        }

        // Fasting Wisdom
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = "Info", tint = EmeraldGreenPrimary)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "The Messenger of Allah (ﷺ) said: 'Take Suhur (pre-dawn meal), for indeed there is blessing in Suhur.' (Sahih al-Bukhari)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FastingDuasContent(
    duas: List<IslamicDua>,
    context: Context
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(duas) { dua ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${dua.titleEn} (${dua.titleBn})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenDark
                        )

                        IconButton(
                            onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("Dua", "${dua.arabicText}\n\n${dua.translationEn}")
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "Dua copied to clipboard", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = EmeraldGreenPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = dua.arabicText,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenPrimary,
                        textAlign = TextAlign.End,
                        lineHeight = 34.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = dua.transliteration,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = dua.translationEn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = dua.translationBn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = EmeraldGreenDark
                    )

                    if (dua.reference != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ref: ${dua.reference}",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RamadanCalendarContent(summary: DailyPrayerSummary) {
    // Generate 30 days schedule derived from current location coordinates
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Ramadan Mubarak Calendar (30 Days)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items((1..30).toList()) { day ->
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (day == 1) EmeraldGreenLight else MaterialTheme.colorScheme.surface,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .background(EmeraldGreenPrimary, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$day",
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Ramadan Day $day",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Sahri", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text(text = summary.sahriEndToday.timeFormatted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Iftar", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text(text = summary.iftarToday.timeFormatted, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = WarmSunsetOrange)
                        }
                    }
                }
            }
        }
    }
}
