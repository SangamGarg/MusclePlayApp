package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.network

import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieRequirementsResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CaloiresInFoodService {
    @Headers(
        "X-Api-Key: b+iPwAySewJoY0fQMO1A4w==5pBH4oEsO1PGWGFf"
    )
    @GET("nutrition")
    fun callCaloriesInFoodApi(
        @Query("query") query: String?,
    ): Call<CaloriesInFoodResponseModel>
}