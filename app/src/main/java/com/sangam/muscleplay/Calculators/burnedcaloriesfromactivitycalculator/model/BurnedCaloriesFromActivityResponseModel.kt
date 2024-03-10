package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model

import com.google.gson.annotations.SerializedName

class BurnedCaloriesFromActivityResponseModel : ArrayList<BurnedCaloriesFromActivityModelItem>()
data class BurnedCaloriesFromActivityModelItem(
    @SerializedName("calories_per_hour") val calories_per_hour: String? = null,
    @SerializedName("duration_minutes") val duration_minutes: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("total_calories") val total_calories: String? = null
)