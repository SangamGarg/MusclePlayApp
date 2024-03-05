package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.network.CaloiresInFoodService
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotRequestBodyModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotResponseModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.network.AiChatBotNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiChatBotRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var aiChatBotResponse = MutableLiveData<AiChatBotResponseModel>()
    fun aiChatBotApiResponse(
        authorization: String,
        contentType: String,
        aiChatBotRequestBodyModel: AiChatBotRequestBodyModel
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.CHATBOT)
            .create(AiChatBotNetworkService::class.java)
        val call = client.callAiChatBotApi(contentType, authorization, aiChatBotRequestBodyModel)
        call.enqueue(object : Callback<AiChatBotResponseModel> {
            override fun onResponse(
                call: Call<AiChatBotResponseModel>,
                response: Response<AiChatBotResponseModel>
            ) {
                Log.d("aiChatBotResponse", response.toString())
                showProgress.postValue(false)
                val body = response.body()
                Log.d("aiChatBotResponse", body.toString())
                if (response.isSuccessful) {
                    aiChatBotResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<AiChatBotResponseModel>, t: Throwable) {
                Log.d("aiChatBotResponse", t.message.toString())

                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }


}