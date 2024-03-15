package com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.network

import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.model.RecipesResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RecipesNetworkService {
    @Headers(
        "X-Api-Key: b+iPwAySewJoY0fQMO1A4w==7jd2HY04eYkJxypI"
    )
    @GET("recipe")
    fun callRecepiesApi(
        @Query("query") query: String?,
    ): Call<RecipesResponseModel>
}