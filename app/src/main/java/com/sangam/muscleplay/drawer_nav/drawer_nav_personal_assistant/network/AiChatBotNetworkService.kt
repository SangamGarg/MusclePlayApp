package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.network

import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotRequestBodyModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AiChatBotNetworkService {
    @POST("completions")
    fun callAiChatBotApi(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") authorization: String,
        @Body aiChatBotRequestModel: AiChatBotRequestBodyModel,
    ): Call<AiChatBotResponseModel>
}