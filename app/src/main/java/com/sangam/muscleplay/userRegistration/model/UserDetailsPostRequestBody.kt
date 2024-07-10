package com.sangam.muscleplay.userRegistration.model

import com.google.gson.annotations.SerializedName

data class UserDetailsPostRequestBody(
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null
)
