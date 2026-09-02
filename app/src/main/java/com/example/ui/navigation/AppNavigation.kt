package com.example.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.LocationPickerBottomSheet
import com.example.ui.screens.AsmaUlHusnaScreen
import com.example.ui.screens.DuaDhikrScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.IslamicVideosScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PrayerTimesScreen
import com.example.ui.screens.QiblaScreen
import com.example.ui.screens.QuranScreen
import com.example.ui.screens.SahriIftarScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TasbihScreen
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.viewmodel.MainViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Default.Mosque)
    object PrayerTimes : Screen("prayer_times", "Prayer Times", Icons.Default.AccessTime)
    object Quran : Screen("quran", "Al-Quran", Icons.Default.MenuBook)
    object Tasbih : Screen("tasbih", "Tasbih", Icons.Default.TouchApp)
    object Qibla : Screen("qibla", "Qibla", Icons.Default.Explore)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    // Additional Routes
    object SahriIftar : Screen("sahri_iftar", "Sahri & Iftar", Icons.Default.AccessTime)
    object AsmaUlHusna : Screen("asma_ul_husna", "99 Names", Icons.Default.MenuBook)
    object DuaDhikr : Screen("dua_dhikr", "Dua & Dhikr", Icons.Default.Mosque)
    object IslamicVideos : Screen("islamic_videos", "Videos", Icons.Default.MenuBook)
    object LiveStream : Screen("live_stream", "Live 24/7", Icons.Default.MenuBook)
    object Onboarding : Screen("onboarding", "Onboarding", Icons.Default.Mosque)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.PrayerTimes,
    Screen.Quran,
    Screen.Tasbih,
    Screen.Settings
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val settings by viewModel.settingsFlow.collectAsState()
    val location by viewModel.locationFlow.collectAsState()
    val translations by viewModel.translations.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showLocationPickerSheet by remember { mutableStateOf(false) }
    val locationSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val startDestination = if (settings.isOnboardingComplete) Screen.Home.route else Screen.Onboarding.route

    val showBottomBar = currentRoute in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = EmeraldGreenPrimary
                ) {
                    bottomNavItems.forEach { screen ->
                        val selected = currentRoute == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title
                                )
                            },
                            label = {
                                Text(
                                    text = when (screen) {
                                        Screen.Home -> translations.home
                                        Screen.PrayerTimes -> translations.prayerTimes
                                        Screen.Quran -> translations.quran
                                        Screen.Tasbih -> translations.tasbih
                                        Screen.Settings -> translations.settings
                                        else -> screen.title
                                    },
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = EmeraldGreenDark,
                                selectedTextColor = EmeraldGreenDark,
                                indicatorColor = EmeraldGreenLight,
                                unselectedIconColor = TextMuted,
                                unselectedTextColor = TextMuted
                            ),
                            modifier = Modifier.testTag("bottom_nav_${screen.route}")
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(
                    currentSettings = settings,
                    currentLocation = location,
                    onFinishOnboarding = { newSettings, newLoc ->
                        viewModel.updateSettings(newSettings)
                        viewModel.setLocation(newLoc)
                        viewModel.completeOnboarding()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(Screen.PrayerTimes.route) {
                PrayerTimesScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Quran.route) {
                QuranScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.SahriIftar.route) {
                SahriIftarScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Tasbih.route) {
                TasbihScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Qibla.route) {
                QiblaScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.AsmaUlHusna.route) {
                AsmaUlHusnaScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.DuaDhikr.route) {
                DuaDhikrScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.IslamicVideos.route) {
                IslamicVideosScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    initialTab = 1
                )
            }

            composable(Screen.LiveStream.route) {
                IslamicVideosScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    initialTab = 0
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onOpenLocationPicker = { showLocationPickerSheet = true }
                )
            }
        }

        // Global Location Picker bottom sheet
        if (showLocationPickerSheet) {
            LocationPickerBottomSheet(
                currentLocation = location,
                translations = translations,
                sheetState = locationSheetState,
                onDismiss = { showLocationPickerSheet = false },
                onLocationSelected = { loc ->
                    viewModel.setLocation(loc)
                }
            )
        }
    }
}
