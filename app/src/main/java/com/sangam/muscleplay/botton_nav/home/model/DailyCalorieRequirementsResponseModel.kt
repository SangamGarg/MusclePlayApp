package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class DailyCalorieRequirementsResponseModel(
    @SerializedName("data") val data: BmiData? = null,
    @SerializedName("request_result") val request_result: String? = null,
    @SerializedName("status_code") val status_code: Int? = null
)

data class DailyCalorieRequirementsData(
    @SerializedName("BMR") val BMR: Int? = null,
    @SerializedName("goals") val goals: DailyCalorieGoals? = null
)