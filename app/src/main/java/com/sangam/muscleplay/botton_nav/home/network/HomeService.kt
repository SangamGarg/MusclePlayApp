package com.sangam.muscleplay.botton_nav.home.network

import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.botton_nav.home.model.IdealWeightResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface HomeService {
    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("bmi")
    fun callBmiApi(
        @Query("age") age: String?,
        @Query("weight") weight: String?,
        @Query("height") height: String?

    ): Call<BmiResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("idealweight")
    fun callIdealWeightApi(
        @Query("gender") gender: String?, @Query("height") height: String?

    ): Call<IdealWeightResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("dailycalorie")
    fun callDailyCalorieRequirementsApi(
        @Query("age") age: String?,
        @Query("gender") gender: String?,
        @Query("height") height: String?,
        @Query("weight") weight: String?,
        @Query("activitylevel") activitylevel: String?,
    ): Call<DailyCalorieRequirementsResponseModel>

    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    @GET("bodyfat")
    fun callBodyFatPercentageApi(
        @Query("age") age: String?,
        @Query("gender") gender: String?,
        @Query("weight") weight: String?,
        @Query("height") height: String?,
        @Query("neck") neck: String?,
        @Query("waist") waist: String?,
        @Query("hip") hip: String?,
    ): Call<BodyFatPercentageResponseModel>
}