package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class DailyCalorieGoals(
    @SerializedName("Extreme weight gain") val extremeWeightGain: WeightGain? = null,
    @SerializedName("Extreme weight loss") val extremeWeightLoss: WeightLoss? = null,
    @SerializedName("Mild weight gain") val mildWeightGain: WeightGain? = null,
    @SerializedName("Mild weight loss") val mildWeightLoss: WeightLoss? = null,
    @SerializedName("Weight gain") val weightGain: WeightGain? = null,
    @SerializedName("Weight loss") val weightLoss: WeightLoss? = null,
    @SerializedName("maintain weight") val maintainweight: Double? = null
)
data class WeightGain(
    @SerializedName("calory") val calory: Double? = null,
    @SerializedName("gain weight") val gainWeight: String? = null
)
data class WeightLoss(
    @SerializedName("calory") val calory: Double? = null,
    @SerializedName("loss weight") val lossWeight: String? = null
)