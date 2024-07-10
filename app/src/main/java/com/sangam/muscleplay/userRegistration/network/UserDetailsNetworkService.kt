package com.sangam.muscleplay.userRegistration.network

import com.example.muscleplay.AppUtils.AppUrls
import com.sangam.muscleplay.AppUtils.AppEndpoints
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserDetailsNetworkService {
    @GET(AppEndpoints.USER_DETAILS + "/{userId}")
    fun getUserDetails(
        @Path("userId") userId: String?
    ): Call<UserDetailsResponseModel>

    @PUT(AppEndpoints.USER_DETAILS + "/{userId}")
    fun putUserDetails(
        @Path("userId") userId: String?,
        @Body userDetailsResponseModel: UserDetailsResponseModel?
    ): Call<UserDetailsResponseModel>

    @POST(AppEndpoints.USER_DETAILS)
    fun postUserDetails(
        @Body userDetailsPostRequestBody: UserDetailsPostRequestBody?
    ): Call<UserDetailsResponseModel>

}