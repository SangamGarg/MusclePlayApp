package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class BodyFatPercentageResponseModel(
    @SerializedName("data") val data: BodyFatPercentageData? = null,
    @SerializedName("request_result") val request_result: String? = null,
    @SerializedName("status_code") val status_code: Int? = null
)

data class BodyFatPercentageData(
    @SerializedName("Body Fat (BMI method)") val bodyFatBMIMethod: String? = null,
    @SerializedName("Body Fat (U.S. Navy Method)") val bodyFatUSNavyMethod: String? = null,
    @SerializedName("Body Fat Category") val bodyFatCategory: String? = null,
    @SerializedName("Body Fat Mass") val bodyFatMass: String? = null,
    @SerializedName("Lean Body Mass") val leanBodyMass: String? = null
)