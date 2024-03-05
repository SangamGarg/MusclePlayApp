package com.sangam.muscleplay.recipes.model

import com.google.gson.annotations.SerializedName

class RecipesResponseModel : ArrayList<RecipesResponseModelItem>()
data class RecipesResponseModelItem(
    @SerializedName("ingredients") val ingredients: String? = null,
    @SerializedName("instructions") val instructions: String? = null,
    @SerializedName("servings") val servings: String? = null,
    @SerializedName("title") val title: String? = null
)