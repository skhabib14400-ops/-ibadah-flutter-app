package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.BangladeshLocationsData
import com.example.model.BangladeshDistrict
import com.example.model.BangladeshDivision
import com.example.model.GlobalCity
import com.example.model.UserLocation
import com.example.ui.localization.TranslationStrings
import com.example.ui.theme.EmeraldGreenDark
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.EmeraldGreenPrimary
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.PureWhite
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationPickerBottomSheet(
    currentLocation: UserLocation,
    translations: TranslationStrings,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onLocationSelected: (UserLocation) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        var selectedTab by remember { mutableIntStateOf(0) } // 0: Bangladesh, 1: Global Cities

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = EmeraldGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = translations.selectLocation,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick GPS Auto-Detect Button
            Button(
                onClick = {
                    val gpsLocation = UserLocation(
                        cityName = "Current GPS Location",
                        countryName = "Bangladesh",
                        latitude = 23.8103,
                        longitude = 90.4125,
                        isGpsAuto = true
                    )
                    onLocationSelected(gpsLocation)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("use_gps_location_btn"),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenPrimary),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.GpsFixed,
                    contentDescription = "GPS",
                    tint = PureWhite,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = translations.useGps,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tab Row (Bangladesh vs Global)
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = EmeraldGreenPrimary,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(translations.bangladeshLocations, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(translations.globalCities, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selectedTab == 0) {
                BangladeshCascadingSelector(
                    currentLocation = currentLocation,
                    translations = translations,
                    onSave = { loc: UserLocation ->
                        onLocationSelected(loc)
                        onDismiss()
                    }
                )
            } else {
                GlobalCitiesSelector(
                    translations = translations,
                    onSelectCity = { city ->
                        val loc = UserLocation(
                            cityName = city.nameEn,
                            countryName = city.countryEn,
                            latitude = city.latitude,
                            longitude = city.longitude,
                            isGpsAuto = false
                        )
                        onLocationSelected(loc)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BangladeshCascadingSelector(
    currentLocation: UserLocation,
    translations: TranslationStrings,
    onSave: (UserLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val divisions = BangladeshLocationsData.divisions

    var selectedDivision by remember {
        mutableStateOf(divisions.find { it.nameEn.equals(currentLocation.divisionName, ignoreCase = true) } ?: divisions.first())
    }

    var selectedDistrict by remember(selectedDivision) {
        mutableStateOf(
            selectedDivision.districts.find { it.nameEn.equals(currentLocation.districtName, ignoreCase = true) }
                ?: selectedDivision.districts.first()
        )
    }

    var selectedUpazila by remember(selectedDistrict) {
        mutableStateOf(
            selectedDistrict.upazilas.find { it.equals(currentLocation.upazilaName, ignoreCase = true) }
                ?: selectedDistrict.upazilas.first()
        )
    }

    var divisionExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }
    var upazilaExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Division Dropdown
        ExposedDropdownMenuBox(
            expanded = divisionExpanded,
            onExpandedChange = { divisionExpanded = !divisionExpanded }
        ) {
            OutlinedTextField(
                value = "${selectedDivision.nameEn} (${selectedDivision.nameBn})",
                onValueChange = {},
                readOnly = true,
                label = { Text(translations.division) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = divisionExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreenPrimary,
                    focusedLabelColor = EmeraldGreenPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("division_dropdown")
            )
            ExposedDropdownMenu(
                expanded = divisionExpanded,
                onDismissRequest = { divisionExpanded = false }
            ) {
                divisions.forEach { div ->
                    DropdownMenuItem(
                        text = { Text("${div.nameEn} - ${div.nameBn}") },
                        onClick = {
                            selectedDivision = div
                            divisionExpanded = false
                        }
                    )
                }
            }
        }

        // 2. District Dropdown
        ExposedDropdownMenuBox(
            expanded = districtExpanded,
            onExpandedChange = { districtExpanded = !districtExpanded }
        ) {
            OutlinedTextField(
                value = "${selectedDistrict.nameEn} (${selectedDistrict.nameBn})",
                onValueChange = {},
                readOnly = true,
                label = { Text(translations.district) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = districtExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreenPrimary,
                    focusedLabelColor = EmeraldGreenPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("district_dropdown")
            )
            ExposedDropdownMenu(
                expanded = districtExpanded,
                onDismissRequest = { districtExpanded = false }
            ) {
                selectedDivision.districts.forEach { dist ->
                    DropdownMenuItem(
                        text = { Text("${dist.nameEn} - ${dist.nameBn}") },
                        onClick = {
                            selectedDistrict = dist
                            districtExpanded = false
                        }
                    )
                }
            }
        }

        // 3. Upazila / Thana Dropdown
        ExposedDropdownMenuBox(
            expanded = upazilaExpanded,
            onExpandedChange = { upazilaExpanded = !upazilaExpanded }
        ) {
            OutlinedTextField(
                value = selectedUpazila,
                onValueChange = {},
                readOnly = true,
                label = { Text(translations.upazila) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = upazilaExpanded) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = EmeraldGreenPrimary,
                    focusedLabelColor = EmeraldGreenPrimary
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .testTag("upazila_dropdown")
            )
            ExposedDropdownMenu(
                expanded = upazilaExpanded,
                onDismissRequest = { upazilaExpanded = false }
            ) {
                selectedDistrict.upazilas.forEach { upazila ->
                    DropdownMenuItem(
                        text = { Text(upazila) },
                        onClick = {
                            selectedUpazila = upazila
                            upazilaExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Save Button
        Button(
            onClick = {
                val loc = UserLocation(
                    cityName = selectedDistrict.nameEn,
                    countryName = "Bangladesh",
                    divisionName = selectedDivision.nameEn,
                    districtName = selectedDistrict.nameEn,
                    upazilaName = selectedUpazila,
                    latitude = selectedDistrict.latitude,
                    longitude = selectedDistrict.longitude,
                    isGpsAuto = false
                )
                onSave(loc)
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("save_bd_location_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenPrimary),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "${translations.save} (${selectedUpazila}, ${selectedDistrict.nameEn})",
                fontWeight = FontWeight.Bold,
                color = PureWhite
            )
        }
    }
}

@Composable
fun GlobalCitiesSelector(
    translations: TranslationStrings,
    onSelectCity: (GlobalCity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val allCities = BangladeshLocationsData.globalCities
    val filtered = remember(searchQuery) {
        if (searchQuery.isBlank()) allCities else allCities.filter {
            it.nameEn.contains(searchQuery, ignoreCase = true) ||
            it.countryEn.contains(searchQuery, ignoreCase = true) ||
            it.nameBn.contains(searchQuery) ||
            it.nameAr.contains(searchQuery)
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(translations.search) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = EmeraldGreenPrimary,
                focusedLeadingIconColor = EmeraldGreenPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("global_city_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filtered) { city ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onSelectCity(city) },
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${city.nameEn} (${city.nameAr})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${city.countryEn} (${city.countryBn})",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Select",
                            tint = EmeraldGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}
