package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

data class DhikrPreset(
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningBn: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val count by viewModel.tasbihCountFlow.collectAsState()
    val target by viewModel.tasbihTargetFlow.collectAsState()
    val total by viewModel.tasbihTotalFlow.collectAsState()
    val dhikrIndex by viewModel.tasbihDhikrIndex.collectAsState()
    val translations by viewModel.translations.collectAsState()

    val dhikrPresets = listOf(
        DhikrPreset("سُبْحَانَ اللَّهِ", "Subḥānallāh", "Glory be to Allah", "আল্লাহ পবিত্র ও নিষ্কলঙ্ক"),
        DhikrPreset("الْحَمْدُ لِلَّهِ", "Al-ḥamdu lillāh", "All praise is for Allah", "সমস্ত প্রশংসা আল্লাহর জন্য"),
        DhikrPreset("اللَّهُ أَكْبَرُ", "Allāhu Akbar", "Allah is the Greatest", "আল্লাহ মহান"),
        DhikrPreset("لَا إِلَهَ إِلَّا اللَّهُ", "Lā ilāha illallāh", "There is no god but Allah", "আল্লাহ ছাড়া কোনো সত্য উপাস্য নেই"),
        DhikrPreset("أَسْتَغْفِرُ اللَّهَ", "Astaghfirullāh", "I seek Allah's forgiveness", "আমি আল্লাহর কাছে ক্ষমা চাই"),
        DhikrPreset("سُبْحَانَ اللَّهِ وَبِحَمْدِهِ", "Subḥānallāhi wa biḥamdih", "Glory and praise be to Allah", "আল্লাহর প্রশংসা সহ তাঁর পবিত্রতা ঘোষণা করি")
    )

    val currentDhikr = dhikrPresets.getOrElse(dhikrIndex) { dhikrPresets[0] }
    val progress = (count.toFloat() / target.toFloat()).coerceIn(0f, 1f)

    var isPressed by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 400f),
        label = "tasbih_scale"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = translations.tasbih,
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
                actions = {
                    IconButton(onClick = { viewModel.resetTasbih() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Selected Dhikr Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentDhikr.arabic,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreenPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = currentDhikr.transliteration,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${currentDhikr.meaningEn} • ${currentDhikr.meaningBn}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Dhikr Selector Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    dhikrPresets.take(3).forEachIndexed { idx, preset ->
                        FilterChip(
                            selected = dhikrIndex == idx,
                            onClick = { viewModel.setTasbihDhikr(idx) },
                            label = { Text(preset.transliteration.take(12), fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldGreenLight,
                                selectedLabelColor = EmeraldGreenDark
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Interactive Circular Bead Button
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .scale(buttonScale)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            viewModel.incrementTasbih()
                            isPressed = true
                        }
                    )
                    .testTag("tasbih_main_tap_btn"),
                contentAlignment = Alignment.Center
            ) {
                // Background Radial Glow
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(EmeraldGreenPrimary, EmeraldGreenDark)
                            ),
                            shape = CircleShape
                        )
                )

                // Outer Circular Progress Ring
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.size(220.dp),
                    color = IslamicGoldLight,
                    strokeWidth = 8.dp,
                    trackColor = PureWhite.copy(alpha = 0.2f)
                )

                // Inner Counter Display
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "$count",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        color = PureWhite
                    )
                    Text(
                        text = "of $target",
                        style = MaterialTheme.typography.titleSmall,
                        color = IslamicGoldLight,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "TAP HERE",
                        style = MaterialTheme.typography.labelSmall,
                        color = PureWhite.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Target Selectors (33, 99, 100, 1000)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(33, 99, 100, 1000).forEach { tgt ->
                    val isSelected = target == tgt
                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { viewModel.setTasbihTarget(tgt) },
                        color = if (isSelected) EmeraldGreenPrimary else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$tgt",
                            color = if (isSelected) PureWhite else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lifetime & Session Statistics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
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
                            text = "Lifetime Total Count",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextSecondary
                        )
                        Text(
                            text = "$total Dhikr",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenDark
                        )
                    }

                    OutlinedButton(
                        onClick = { viewModel.resetTasbih() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset")
                    }
                }
            }
        }
    }
}
