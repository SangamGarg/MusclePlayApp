package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class IdealWeightResponseModel(
    @SerializedName("data") val data: IdealWeightData? = null,
    @SerializedName("request_result") val request_result: String? = null,
    @SerializedName("status_code") val status_code: Int? = null
)

data class IdealWeightData(
    @SerializedName("Devine") val devine: Double? = null,
    @SerializedName("Hamwi") val hamwi: Double? = null,
    @SerializedName("Miller") val miller: Double? = null,
    @SerializedName("Robinson") val robinson: Double? = null
)