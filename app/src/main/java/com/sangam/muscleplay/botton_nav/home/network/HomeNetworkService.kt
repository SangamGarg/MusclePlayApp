package com.sangam.muscleplay.botton_nav.home.network

import com.sangam.muscleplay.appUtils.AppEndpoints
import com.sangam.muscleplay.botton_nav.home.model.HomeRequestBody
import com.sangam.muscleplay.botton_nav.home.model.HomeResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface HomeNetworkService {
    @POST(AppEndpoints.HOME)
    fun callHomeApi(
        @Body homeRequestBody: HomeRequestBody?
    ): Call<HomeResponseModel>
}