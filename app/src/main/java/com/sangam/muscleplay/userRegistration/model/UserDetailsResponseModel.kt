package com.sangam.muscleplay.userRegistration.model

import com.google.gson.annotations.SerializedName

data class UserDetailsResponseModel(

    @SerializedName("userId") val userId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("profileImageUrl") val profileImageUrl: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("age") val age: Int? = null,
    @SerializedName("measurements") val measurements: Measurements? = Measurements()
)

data class Measurements(

    @SerializedName("height") val height: Int? = null,
    @SerializedName("weight") val weight: Int? = null,
    @SerializedName("activityLevel") val activityLevel: String? = null,
    @SerializedName("goal") val goal: String? = null,
    @SerializedName("hip") val hip: Int? = null,
    @SerializedName("neck") val neck: Int? = null,
    @SerializedName("waist") val waist: Int? = null

)