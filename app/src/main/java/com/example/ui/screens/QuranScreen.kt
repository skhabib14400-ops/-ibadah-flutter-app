package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.QuranRepository
import com.example.model.Ayah
import com.example.model.QuranReciter
import com.example.model.Surah
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val translations by viewModel.translations.collectAsState()
    val bookmarkedSurahs by viewModel.bookmarkedSurahNumbers.collectAsState()
    val isPlayingAudio by viewModel.isPlayingAudio.collectAsState()
    val playingSurahNumber by viewModel.playingSurahNumber.collectAsState()
    val playingAyahNumber by viewModel.playingAyahNumber.collectAsState()
    val activeAyahIndex by viewModel.activeAyahIndex.collectAsState()
    val selectedReciterIndex by viewModel.selectedReciterIndex.collectAsState()
    val isFullSurahMode by viewModel.isFullSurahMode.collectAsState()
    val surahAyahsMap by viewModel.surahAyahsMap.collectAsState()
    val isLoadingAyahs by viewModel.isLoadingAyahs.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSurahForReading by remember { mutableStateOf<Surah?>(null) }
    var arabicFontSizeSp by remember { mutableFloatStateOf(24f) }

    val allSurahs = QuranRepository.surahs
    val filteredSurahs = remember(searchQuery) {
        val q = searchQuery.trim()
        if (q.isBlank()) allSurahs else allSurahs.filter {
            it.number.toString() == q ||
            it.nameEnglish.contains(q, ignoreCase = true) ||
            it.nameBengali.contains(q) ||
            it.nameArabic.contains(q) ||
            it.englishMeaning.contains(q, ignoreCase = true) ||
            it.bengaliMeaning.contains(q)
        }
    }

    // Handle back button when inside Surah detail view
    BackHandler(enabled = selectedSurahForReading != null) {
        selectedSurahForReading = null
    }

    if (selectedSurahForReading != null) {
        val surah = selectedSurahForReading!!
        
        // Trigger live dynamic fetch for all ayahs without pagination limits
        LaunchedEffect(surah.number) {
            viewModel.loadSurahAyahs(surah.number)
        }

        val ayahs = surahAyahsMap[surah.number] ?: remember(surah.number) { QuranRepository.getAyahsForSurah(surah.number) }

        SurahDetailReaderView(
            surah = surah,
            ayahs = ayahs,
            isLoadingAyahs = isLoadingAyahs,
            isBookmarked = bookmarkedSurahs.contains(surah.number),
            isPlayingAudio = isPlayingAudio,
            playingSurahNumber = playingSurahNumber,
            playingAyahNumber = playingAyahNumber,
            activeAyahIndex = activeAyahIndex,
            selectedReciterIndex = selectedReciterIndex,
            isFullSurahMode = isFullSurahMode,
            arabicFontSizeSp = arabicFontSizeSp,
            onIncreaseFontSize = { arabicFontSizeSp = (arabicFontSizeSp + 2f).coerceAtMost(36f) },
            onDecreaseFontSize = { arabicFontSizeSp = (arabicFontSizeSp - 2f).coerceAtLeast(18f) },
            onBookmarkToggle = { viewModel.toggleBookmarkSurah(surah.number) },
            onPlayFullSurah = {
                if (isPlayingAudio && playingSurahNumber == surah.number && isFullSurahMode) {
                    viewModel.togglePlayAudio()
                } else {
                    viewModel.playSurah(surah.number)
                }
            },
            onPlayAyah = { ayahNum ->
                if (isPlayingAudio && playingSurahNumber == surah.number && playingAyahNumber == ayahNum) {
                    viewModel.togglePlayAudio()
                } else {
                    viewModel.playAyah(surah.number, ayahNum)
                }
            },
            onTogglePlayPause = { viewModel.togglePlayAudio() },
            onNextAyah = { viewModel.nextAyah(ayahs.size) },
            onPreviousAyah = { viewModel.previousAyah() },
            onStopAudio = { viewModel.stopAudio() },
            onSelectReciter = { viewModel.setReciterIndex(it) },
            onBack = { selectedSurahForReading = null }
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "${translations.quran} (114 Surahs)",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "আল-কুরআনুল কারীম • Complete 114 Surahs",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureWhite.copy(alpha = 0.85f)
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
                    .padding(16.dp)
            ) {
                // Search Input Field
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search Surah by name (Fatihah / ফাতিহা) or number (1-114)...",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = EmeraldGreenPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextMuted
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreenPrimary,
                        focusedLeadingIconColor = EmeraldGreenPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quran_search_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Quick stats banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = EmeraldGreenLight,
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = EmeraldGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Showing ${filteredSurahs.size} of 114 Surahs",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = EmeraldGreenDark
                            )
                        }

                        if (isPlayingAudio && playingSurahNumber != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(IslamicGold.copy(alpha = 0.15f))
                                    .clickable {
                                        val playing = allSurahs.firstOrNull { it.number == playingSurahNumber }
                                        if (playing != null) selectedSurahForReading = playing
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MusicNote,
                                    contentDescription = "Playing",
                                    tint = IslamicGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Surah $playingSurahNumber Playing",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldGreenDark
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (filteredSurahs.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No Surah found matching \"$searchQuery\"",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextSecondary
                            )
                        }
                    }
                } else {
                    // Surah List - 114 Surahs smooth scrolling
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .testTag("surah_list_view"),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredSurahs, key = { it.number }) { surah ->
                            val isBookmarked = bookmarkedSurahs.contains(surah.number)
                            val isThisSurahPlaying = isPlayingAudio && playingSurahNumber == surah.number

                            SurahCardItem(
                                surah = surah,
                                isBookmarked = isBookmarked,
                                isPlaying = isThisSurahPlaying,
                                onBookmarkToggle = { viewModel.toggleBookmarkSurah(surah.number) },
                                onClick = { selectedSurahForReading = surah }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SurahCardItem(
    surah: Surah,
    isBookmarked: Boolean,
    isPlaying: Boolean,
    onBookmarkToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("surah_card_${surah.number}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlaying) EmeraldGreenLight else MaterialTheme.colorScheme.surface
        ),
        border = if (isPlaying) {
            CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, IslamicGold))
            )
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Surah Number Badge (1-114)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            brush = Brush.radialGradient(
                                if (isPlaying) listOf(IslamicGoldLight, IslamicGold)
                                else listOf(EmeraldGreenLight, EmeraldGreenPrimary.copy(alpha = 0.2f))
                            ),
                            shape = CircleShape
                        )
                        .border(
                            1.dp,
                            if (isPlaying) IslamicGold else EmeraldGreenPrimary.copy(alpha = 0.4f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isPlaying) EmeraldGreenDark else EmeraldGreenDark
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    // English & Bengali Name (e.g., Al-Fatihah / আল-ফাতিহা)
                    Text(
                        text = "${surah.nameEnglish} / ${surah.nameBengali}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    // Type (Meccan/Medinan) & Total Ayahs
                    Text(
                        text = "${surah.revelationType} • ${surah.totalVerses} Ayahs (${surah.englishMeaning})",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Arabic Name
                Text(
                    text = surah.nameArabic,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreenPrimary,
                    fontFamily = FontFamily.Default
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onBookmarkToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (isBookmarked) IslamicGold else TextMuted
                    )
                }
            }
        }
    }
}

/**
 * Detailed Surah Reading & Audio Screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahDetailReaderView(
    surah: Surah,
    ayahs: List<Ayah>,
    isLoadingAyahs: Boolean = false,
    isBookmarked: Boolean,
    isPlayingAudio: Boolean,
    playingSurahNumber: Int?,
    playingAyahNumber: Int?,
    activeAyahIndex: Int,
    selectedReciterIndex: Int,
    isFullSurahMode: Boolean,
    arabicFontSizeSp: Float,
    onIncreaseFontSize: () -> Unit,
    onDecreaseFontSize: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onPlayFullSurah: () -> Unit,
    onPlayAyah: (Int) -> Unit,
    onTogglePlayPause: () -> Unit,
    onNextAyah: () -> Unit,
    onPreviousAyah: () -> Unit,
    onStopAudio: () -> Unit,
    onSelectReciter: (Int) -> Unit,
    onBack: () -> Unit
) {
    val reciters = QuranRepository.reciters
    val currentReciter = reciters.getOrElse(selectedReciterIndex) { reciters[0] }
    var showReciterSheet by remember { mutableStateOf(false) }
    var showFontSizeMenu by remember { mutableStateOf(false) }

    val isCurrentSurahPlaying = isPlayingAudio && playingSurahNumber == surah.number
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Auto-scroll when active ayah changes during playback
    LaunchedEffect(activeAyahIndex, isCurrentSurahPlaying) {
        if (isCurrentSurahPlaying && activeAyahIndex in ayahs.indices) {
            coroutineScope.launch {
                listState.animateScrollToItem(activeAyahIndex + 1) // +1 because item 0 is Bismillah
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${surah.number}. ${surah.nameEnglish}",
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${surah.nameBengali})",
                                color = PureWhite.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = "${surah.revelationType} • ${surah.totalVerses} Ayahs • ${surah.bengaliMeaning}",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureWhite.copy(alpha = 0.85f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
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
                actions = {
                    // Font Size Control
                    IconButton(onClick = { showFontSizeMenu = !showFontSizeMenu }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Font Size",
                            tint = PureWhite
                        )
                    }

                    DropdownMenu(
                        expanded = showFontSizeMenu,
                        onDismissRequest = { showFontSizeMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Increase Arabic Text (+)") },
                            onClick = { onIncreaseFontSize() }
                        )
                        DropdownMenuItem(
                            text = { Text("Decrease Arabic Text (-)") },
                            onClick = { onDecreaseFontSize() }
                        )
                        DropdownMenuItem(
                            text = { Text("Current Size: ${arabicFontSizeSp.toInt()}sp") },
                            onClick = { },
                            enabled = false
                        )
                    }

                    // Bookmark
                    IconButton(onClick = onBookmarkToggle) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) IslamicGold else PureWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EmeraldGreenPrimary
                )
            )
        },
        bottomBar = {
            // Audio Player Docked Bar
            SurahBottomAudioPlayer(
                surah = surah,
                ayahCount = ayahs.size,
                isPlaying = isCurrentSurahPlaying,
                currentReciter = currentReciter,
                activeAyahIndex = activeAyahIndex,
                isFullSurahMode = isFullSurahMode,
                playingAyahNumber = playingAyahNumber,
                onPlayFullSurah = onPlayFullSurah,
                onTogglePlayPause = onTogglePlayPause,
                onNextAyah = onNextAyah,
                onPreviousAyah = onPreviousAyah,
                onStopAudio = onStopAudio,
                onOpenReciterSelection = { showReciterSheet = true }
            )
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .testTag("surah_ayah_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live fetch indicator
            if (isLoadingAyahs) {
                item(key = "loading_indicator") {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        color = IslamicGold,
                        trackColor = IslamicGold.copy(alpha = 0.2f)
                    )
                }
            }

            // 1. Bismillah Header Card at the top (All Surahs except Surah 9 At-Tawbah)
            item(key = "bismillah_header") {
                if (surah.number != 9) {
                    BismillahTopBanner()
                } else {
                    AtTawbahNoteBanner()
                }
            }

            // 2. All Ayahs of that Surah
            items(ayahs, key = { it.ayahNumber }) { ayah ->
                val isThisAyahActive = isCurrentSurahPlaying && (
                    (playingAyahNumber == ayah.ayahNumber) ||
                    (isFullSurahMode && (activeAyahIndex + 1) == ayah.ayahNumber)
                )

                AyahDetailCard(
                    ayah = ayah,
                    isActivePlaying = isThisAyahActive,
                    arabicFontSizeSp = arabicFontSizeSp,
                    onPlayAyah = { onPlayAyah(ayah.ayahNumber) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    // Reciter Selection Bottom Sheet
    if (showReciterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showReciterSheet = false },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = EmeraldGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select Quran Reciter (কারী নির্বাচন)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenPrimary
                        )
                    }
                    IconButton(onClick = { showReciterSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                reciters.forEachIndexed { index, reciter ->
                    val isSelected = index == selectedReciterIndex
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                onSelectReciter(index)
                                showReciterSheet = false
                            },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) EmeraldGreenLight else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, IslamicGold))
                        ) else CardDefaults.outlinedCardBorder()
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
                                    text = reciter.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${reciter.subName} • ${reciter.style}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(EmeraldGreenPrimary, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Selected",
                                        tint = PureWhite,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Ornate Bismillah Top Banner
 */
@Composable
private fun BismillahTopBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = EmeraldGreenLight.copy(alpha = 0.6f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(IslamicGold.copy(alpha = 0.7f), EmeraldGreenPrimary.copy(alpha = 0.5f)))
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 28.sp,
                    lineHeight = 44.sp
                ),
                fontWeight = FontWeight.Bold,
                color = EmeraldGreenDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "বিসমিল্লাহির রাহমানির রাহীম",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = EmeraldGreenPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun AtTawbahNoteBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "সূরা আত-তাওবাহ্ (বিসমিল্লাহ ছাড়া শুরু হয়)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Surah At-Tawbah does not begin with Bismillah according to Islamic tradition.",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Individual Ayah Card
 */
@Composable
private fun AyahDetailCard(
    ayah: Ayah,
    isActivePlaying: Boolean,
    arabicFontSizeSp: Float,
    onPlayAyah: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("ayah_card_${ayah.ayahNumber}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActivePlaying) EmeraldGreenLight else MaterialTheme.colorScheme.surface
        ),
        border = if (isActivePlaying) {
            CardDefaults.outlinedCardBorder().copy(
                brush = Brush.linearGradient(listOf(EmeraldGreenPrimary, IslamicGold))
            )
        } else {
            CardDefaults.outlinedCardBorder()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Ayah Number Badge & Audio Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ayah number badge
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isActivePlaying) EmeraldGreenPrimary else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = "Ayah ${ayah.ayahNumber} (আয়াত ${ayah.ayahNumber})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isActivePlaying) PureWhite else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Play Ayah button
                    OutlinedButton(
                        onClick = onPlayAyah,
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isActivePlaying) IslamicGold else EmeraldGreenPrimary
                        )
                    ) {
                        Icon(
                            imageVector = if (isActivePlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "Listen Ayah",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isActivePlaying) "Playing" else "Play",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Arabic text
            Text(
                text = "${ayah.textArabic} ۝${ayah.ayahNumber}",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = arabicFontSizeSp.sp,
                    lineHeight = (arabicFontSizeSp * 1.6f).sp
                ),
                fontWeight = FontWeight.Bold,
                color = EmeraldGreenDark,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(10.dp))

            // 2. Bengali pronunciation (উচ্চারণ)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "উচ্চারণ: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = IslamicGold
                )
                Text(
                    text = ayah.pronunciationBn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Bengali meaning (বাংলা অনুবাদ)
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "অনুবাদ: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreenPrimary
                )
                Text(
                    text = ayah.translationBn,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            if (ayah.translationEn.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "En: ${ayah.translationEn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}

/**
 * Sticky Bottom Audio Player Bar
 */
@Composable
private fun SurahBottomAudioPlayer(
    surah: Surah,
    ayahCount: Int,
    isPlaying: Boolean,
    currentReciter: QuranReciter,
    activeAyahIndex: Int,
    isFullSurahMode: Boolean,
    playingAyahNumber: Int?,
    onPlayFullSurah: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onNextAyah: () -> Unit,
    onPreviousAyah: () -> Unit,
    onStopAudio: () -> Unit,
    onOpenReciterSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("surah_bottom_audio_player"),
        shadowElevation = 12.dp,
        color = MaterialTheme.colorScheme.surface,
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Track Info & Reciter Selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isPlaying) EmeraldGreenPrimary else EmeraldGreenLight,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = "Audio",
                            tint = if (isPlaying) PureWhite else EmeraldGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = if (isPlaying) {
                                if (isFullSurahMode) "Playing: Surah ${surah.nameEnglish} (Ayah ${activeAyahIndex + 1}/$ayahCount)"
                                else "Playing Ayah $playingAyahNumber of ${surah.nameEnglish}"
                            } else {
                                "Surah ${surah.nameEnglish} Audio"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Reciter: ${currentReciter.name}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Reciter Selection Button
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldGreenLight,
                    modifier = Modifier
                        .clickable(onClick = onOpenReciterSelection)
                        .testTag("reciter_selector_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Change Reciter",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Playback Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Full Surah Button
                OutlinedButton(
                    onClick = onPlayFullSurah,
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isPlaying && isFullSurahMode) EmeraldGreenLight else Color.Transparent,
                        contentColor = EmeraldGreenPrimary
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying && isFullSurahMode) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isPlaying && isFullSurahMode) "Pause Surah" else "Play Full Surah",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Center Navigation & Play/Pause
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Previous Ayah
                    IconButton(
                        onClick = onPreviousAyah,
                        enabled = activeAyahIndex > 0,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Previous Ayah",
                            tint = if (activeAyahIndex > 0) EmeraldGreenPrimary else TextMuted
                        )
                    }

                    // Play/Pause Main Button
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(EmeraldGreenPrimary, CircleShape)
                            .clickable(onClick = onTogglePlayPause)
                            .testTag("audio_play_pause_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pause" else "Play",
                            tint = PureWhite,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    // Next Ayah
                    IconButton(
                        onClick = onNextAyah,
                        enabled = activeAyahIndex < (ayahCount - 1),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Ayah",
                            tint = if (activeAyahIndex < (ayahCount - 1)) EmeraldGreenPrimary else TextMuted
                        )
                    }

                    // Stop
                    if (isPlaying) {
                        IconButton(
                            onClick = onStopAudio,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "Stop",
                                tint = TextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
