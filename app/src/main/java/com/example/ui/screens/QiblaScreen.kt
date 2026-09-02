package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActiveGreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.ForbiddenRed
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WarmSunsetOrange
import com.example.viewmodel.MainViewModel
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QiblaScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val location by viewModel.locationFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()
    val qiblaBearing by viewModel.qiblaAngle.collectAsState()
    val deviceHeading by viewModel.deviceHeading.collectAsState()
    val distanceKm by viewModel.distanceToKaabaKm.collectAsState()

    // Calculated relative angle from device heading to Kaaba
    val relativeAngle = ((qiblaBearing - deviceHeading + 360.0) % 360.0).toFloat()
    val isFacingKaaba = abs(relativeAngle) < 5f || abs(relativeAngle - 360f) < 5f

    val animatedRotation by animateFloatAsState(
        targetValue = relativeAngle,
        animationSpec = spring(stiffness = 300f),
        label = "qibla_rotation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = translations.qibla,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Location and Alignment Status Badge
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (isFacingKaaba) ActiveGreen.copy(alpha = 0.15f) else EmeraldGreenLight
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (isFacingKaaba) Icons.Default.CheckCircle else Icons.Default.Explore,
                        contentDescription = "Status",
                        tint = if (isFacingKaaba) ActiveGreen else EmeraldGreenPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFacingKaaba) "ALIGNED WITH KAABA (QIBLA)" else "ROTATE DEVICE TOWARDS KAABA",
                        color = if (isFacingKaaba) EmeraldGreenDark else EmeraldGreenDark,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Compass Dial Canvas
            Box(
                modifier = Modifier
                    .size(280.dp)
                    .testTag("qibla_compass_dial"),
                contentAlignment = Alignment.Center
            ) {
                // Dial Background
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    val radius = size.minDimension / 2 - 12.dp.toPx()

                    // Outer Rim
                    drawCircle(
                        color = if (isFacingKaaba) ActiveGreen else EmeraldGreenPrimary,
                        radius = radius,
                        center = center,
                        style = Stroke(width = 4.dp.toPx())
                    )

                    // Inner background fill
                    drawCircle(
                        color = if (isFacingKaaba) ActiveGreen.copy(alpha = 0.08f) else EmeraldGreenPrimary.copy(alpha = 0.04f),
                        radius = radius - 4.dp.toPx(),
                        center = center
                    )

                    // Draw 12 Cardinal Ticks
                    for (i in 0 until 12) {
                        val angle = i * 30f
                        rotate(angle, center) {
                            val isMajor = i % 3 == 0
                            val tickLength = if (isMajor) 14.dp.toPx() else 8.dp.toPx()
                            drawLine(
                                color = if (isMajor) EmeraldGreenDark else TextMuted,
                                start = Offset(center.x, center.y - radius),
                                end = Offset(center.x, center.y - radius + tickLength),
                                strokeWidth = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
                            )
                        }
                    }
                }

                // Rotating Needle Pointing to Kaaba
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(animatedRotation),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 120.dp)
                    ) {
                        // Kaaba Icon at the tip of the needle
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(if (isFacingKaaba) ActiveGreen else IslamicGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mosque,
                                contentDescription = "Kaaba",
                                tint = PureWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Needle",
                            tint = if (isFacingKaaba) ActiveGreen else EmeraldGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                // Center Pin
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(EmeraldGreenDark, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Qibla Info Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Qibla Bearing Angle:", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = String.format("%.1f° from North", qiblaBearing),
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenDark,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Distance to Kaaba, Makkah:", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "$distanceKm km",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Selected Location:", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = location.displayFormatted,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Coordinates:", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = String.format("%.4f° N, %.4f° E", location.latitude, location.longitude),
                            fontWeight = FontWeight.Medium,
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
