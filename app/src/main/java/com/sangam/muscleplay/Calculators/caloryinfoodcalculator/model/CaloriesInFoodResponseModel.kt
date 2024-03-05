package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model

import com.google.gson.annotations.SerializedName

data class CaloriesInFoodResponseModel(
    @SerializedName("items") val items: ArrayList<CaloriesInFoodItem>? = null
)

data class CaloriesInFoodItem(
    @SerializedName("calories") val calories: String? = null,
    @SerializedName("carbohydrates_total_g") val carbohydrates_total_g: String? = null,
    @SerializedName("cholesterol_mg") val cholesterol_mg: String? = null,
    @SerializedName("fat_saturated_g") val fat_saturated_g: String? = null,
    @SerializedName("fat_total_g") val fat_total_g: String? = null,
    @SerializedName("fiber_g") val fiber_g: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("potassium_mg") val potassium_mg: String? = null,
    @SerializedName("protein_g") val protein_g: String? = null,
    @SerializedName("serving_size_g") val serving_size_g: String? = null,
    @SerializedName("sodium_mg") val sodium_mg: String? = null,
    @SerializedName("sugar_g") val sugar_g: String? = null
)