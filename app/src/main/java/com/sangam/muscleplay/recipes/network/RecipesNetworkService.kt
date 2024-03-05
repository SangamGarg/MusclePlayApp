package com.sangam.muscleplay.recipes.network

import com.sangam.muscleplay.recipes.model.RecipesResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipesNetworkService {
    @Headers(
        "X-Api-Key: b+iPwAySewJoY0fQMO1A4w==5pBH4oEsO1PGWGFf"
    )
    @GET("recipe")
    fun callRecepiesApi(
        @Query("query") query: String?,
    ): Call<RecipesResponseModel>
}