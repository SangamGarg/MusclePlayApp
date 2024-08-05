package com.sangam.muscleplay.botton_nav.home.model

import com.google.gson.annotations.SerializedName

data class HomeRequestBody(

    @SerializedName("age") val age: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("weight") val weight: String? = null,
    @SerializedName("height") val height: String? = null,
    @SerializedName("waist") val waist: String? = null,
    @SerializedName("hip") val hip: String? = null,
    @SerializedName("neck") val neck: String? = null,
    @SerializedName("activityLevel") val activityLevel: String? = null
)