package com.sangam.muscleplay.calculators.caloryinfoodcalculator.network

import com.sangam.muscleplay.calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
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