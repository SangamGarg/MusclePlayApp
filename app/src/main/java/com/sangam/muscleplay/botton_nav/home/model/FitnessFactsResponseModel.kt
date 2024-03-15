package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class FitnessFactsResponseModel(
    @SerializedName("factsHeadline") val factsHeadline: String? = null,
    @SerializedName("factsSummary") val factsSummary: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null
)