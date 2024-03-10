package com.sangam.muscleplay.excercises.model

import com.google.gson.annotations.SerializedName

class ExerciseResponseModel : ArrayList<ExerciseResponseModelItem>()
data class ExerciseResponseModelItem(
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("muscle") val muscle: String? = null,
    @SerializedName("equipment") val equipment: String? = null,
    @SerializedName("difficulty") val difficulty: String? = null,
    @SerializedName("instructions") val instructions: String? = null
)