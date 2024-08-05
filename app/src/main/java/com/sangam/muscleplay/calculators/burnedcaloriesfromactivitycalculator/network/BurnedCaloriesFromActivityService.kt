package com.sangam.muscleplay.calculators.burnedcaloriesfromactivitycalculator.network

import com.sangam.muscleplay.calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface BurnedCaloriesFromActivityService {
    @Headers("X-Api-Key: b+iPwAySewJoY0fQMO1A4w==7jd2HY04eYkJxypI")
    @GET("caloriesburned")
    fun callBurnedCaloriesFromActivityApi(
        @Query("activity") activity: String?,
    ): Call<BurnedCaloriesFromActivityResponseModel>
}