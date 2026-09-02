package com.example.model

data class UserLocation(
    val cityName: String,
    val countryName: String,
    val divisionName: String? = null,
    val districtName: String? = null,
    val upazilaName: String? = null,
    val latitude: Double,
    val longitude: Double,
    val isGpsAuto: Boolean = false
) {
    val displayFormatted: String
        get() {
            return if (!upazilaName.isNullOrEmpty() && !districtName.isNullOrEmpty()) {
                "$upazilaName, $districtName"
            } else if (!districtName.isNullOrEmpty()) {
                "$districtName, $countryName"
            } else {
                "$cityName, $countryName"
            }
        }
}

data class BangladeshDivision(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val districts: List<BangladeshDistrict>
)

data class BangladeshDistrict(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val latitude: Double,
    val longitude: Double,
    val upazilas: List<String>
)

data class GlobalCity(
    val nameEn: String,
    val nameBn: String,
    val nameAr: String,
    val countryEn: String,
    val countryBn: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String
)
