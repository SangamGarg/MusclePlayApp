package com.sangam.muscleplay.botton_nav.home.network

import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface HomeService {
    @GET("bmi")
    @Headers(
        "X-RapidAPI-Key: daea6a4638mshf3440e0cc567bd2p152eacjsn3c832c74a6de",
        "X-RapidAPI-Host: fitness-calculator.p.rapidapi.com"
    )
    fun callBmiApi(
        @Query("age") age: String?,
        @Query("weight") weight: String?,
        @Query("height") height: String?

    ): Call<BmiResponseModel>
}