package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class BmiResponseModel(
    @SerializedName("data") val data: BmiData? = null,
    @SerializedName("request_result") val request_result: String? = null,
    @SerializedName("status_code") val status_code: Int? = null
)

data class BmiData(
    @SerializedName("bmi") val bmi: Double? = null,
    @SerializedName("health") val health: String? = null,
    @SerializedName("healthy_bmi_range") val healthy_bmi_range: String? = null
)