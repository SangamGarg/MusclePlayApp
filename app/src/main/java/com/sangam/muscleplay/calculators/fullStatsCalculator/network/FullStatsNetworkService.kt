package com.sangam.muscleplay.calculators.fullStatsCalculator.network

import com.sangam.muscleplay.calculators.model.BmiResponseModel
import com.sangam.muscleplay.calculators.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.calculators.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.calculators.model.IdealWeightResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FullStatsNetworkService {
    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("bmi")
    suspend fun callBmiApi(
        @Query("age") age: String?,
        @Query("weight") weight: String?,
        @Query("height") height: String?

    ): Response<BmiResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("idealweight")
    suspend fun callIdealWeightApi(
        @Query("gender") gender: String?, @Query("height") height: String?

    ): Response<IdealWeightResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("dailycalorie")
    suspend fun callDailyCalorieRequirementsApi(
        @Query("age") age: String?,
        @Query("gender") gender: String?,
        @Query("height") height: String?,
        @Query("weight") weight: String?,
        @Query("activitylevel") activitylevel: String?,
    ): Response<DailyCalorieRequirementsResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("bodyfat")
    suspend fun callBodyFatPercentageApi(
        @Query("age") age: String?,
        @Query("gender") gender: String?,
        @Query("weight") weight: String?,
        @Query("height") height: String?,
        @Query("neck") neck: String?,
        @Query("waist") waist: String?,
        @Query("hip") hip: String?,
    ): Response<BodyFatPercentageResponseModel>
}