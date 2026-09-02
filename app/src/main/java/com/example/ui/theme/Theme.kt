package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreenPrimary,
    onPrimary = PureWhite,
    primaryContainer = EmeraldGreenDark,
    onPrimaryContainer = EmeraldGreenLight,
    secondary = IslamicGold,
    onSecondary = DeepIslamicNight,
    secondaryContainer = DarkCard,
    onSecondaryContainer = IslamicGoldLight,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkCard,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCardBorder,
    error = ForbiddenRed
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldGreenPrimary,
    onPrimary = PureWhite,
    primaryContainer = EmeraldGreenLight,
    onPrimaryContainer = EmeraldGreenOnContainer,
    secondary = IslamicGold,
    onSecondary = PureWhite,
    secondaryContainer = IslamicGoldLight,
    onSecondaryContainer = IslamicGoldDark,
    background = OffWhiteSurface,
    onBackground = TextPrimary,
    surface = PureWhite,
    onSurface = TextPrimary,
    surfaceVariant = EmeraldGreenLight,
    onSurfaceVariant = TextSecondary,
    outline = LightCardBorder,
    error = ForbiddenRed
)

@Composable
fun IbadahTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    IbadahTheme(darkTheme = darkTheme, content = content)
}


