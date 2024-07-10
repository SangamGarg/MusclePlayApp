package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.sangam.muscleplay.retrofit.RetrofitUtilClass
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.network.CaloiresInFoodService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CaloriesInFoodRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var caloriesInFoodResponse = MutableLiveData<CaloriesInFoodResponseModel>()
    fun caloriesInFoodApiResponse(
        query: String?
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.CALORIESINFOOOD)
            .create(CaloiresInFoodService::class.java)
        var call = client?.callCaloriesInFoodApi(query)
        call?.enqueue(object : Callback<CaloriesInFoodResponseModel> {
            override fun onResponse(
                call: Call<CaloriesInFoodResponseModel>,
                response: Response<CaloriesInFoodResponseModel>
            ) {
                Log.d("CaloriesInFoodReponse", response.toString())
                showProgress.postValue(false)
                val body = response.body()
                Log.d("CaloriesInFoodReponse", body.toString())
                if (response.isSuccessful) {
                    caloriesInFoodResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<CaloriesInFoodResponseModel>, t: Throwable) {
                Log.d("CaloriesInFoodReponse", t.message.toString())

                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }

}