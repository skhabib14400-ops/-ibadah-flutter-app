package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DuaRepository
import com.example.model.DuaCategory
import com.example.model.IslamicDua
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.ForbiddenRed
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuaDhikrScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val translations by viewModel.translations.collectAsState()
    val selectedCategory by viewModel.selectedDuaCategory.collectAsState()
    val favoriteIds by viewModel.favoriteDuaIds.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    val allDuas = DuaRepository.duas
    val filteredDuas = remember(selectedCategory, searchQuery) {
        allDuas.filter { dua ->
            (searchQuery.isBlank() && dua.category == selectedCategory) ||
            (searchQuery.isNotBlank() && (
                dua.titleEn.contains(searchQuery, ignoreCase = true) ||
                dua.titleBn.contains(searchQuery) ||
                dua.translationEn.contains(searchQuery, ignoreCase = true) ||
                dua.arabicText.contains(searchQuery)
            ))
        }
    }

    val categories = DuaCategory.entries

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = translations.duaDhikr,
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
            // Search Input
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(translations.search) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = EmeraldGreenPrimary) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreenPrimary,
                    focusedLeadingIconColor = EmeraldGreenPrimary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag("dua_search_input")
            )

            // Category Tabs
            PrimaryScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory).coerceAtLeast(0),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = EmeraldGreenPrimary,
                edgePadding = 16.dp
            ) {
                categories.forEach { cat ->
                    Tab(
                        selected = selectedCategory == cat,
                        onClick = {
                            searchQuery = ""
                            viewModel.setDuaCategory(cat)
                        },
                        text = { Text(cat.titleEn, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Duas Stream
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredDuas) { dua ->
                    val isFav = favoriteIds.contains(dua.id)
                    DuaItemCard(
                        dua = dua,
                        isFavorite = isFav,
                        onFavoriteToggle = { viewModel.toggleFavoriteDua(dua.id) },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dua", "${dua.arabicText}\n\n${dua.translationEn}")
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Dua copied to clipboard", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DuaItemCard(
    dua: IslamicDua,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("dua_card_${dua.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    color = EmeraldGreenDark,
                    modifier = Modifier.weight(1f)
                )

                Row {
                    IconButton(onClick = onFavoriteToggle, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorite) ForbiddenRed else TextMuted
                        )
                    }
                    IconButton(onClick = onCopy, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = EmeraldGreenPrimary
                        )
                    }
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
