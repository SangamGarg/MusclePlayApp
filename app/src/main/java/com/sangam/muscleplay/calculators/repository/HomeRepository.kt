package com.sangam.muscleplay.calculators.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.sangam.muscleplay.retrofit.RetrofitUtilClass
import com.sangam.muscleplay.calculators.model.BmiResponseModel
import com.sangam.muscleplay.calculators.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.calculators.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.calculators.model.IdealWeightResponseModel
import com.sangam.muscleplay.calculators.network.HomeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    val showProgressBmi = MutableLiveData<Boolean>()
    val showProgressIdealWeight = MutableLiveData<Boolean>()
    val showProgressDalyCalories = MutableLiveData<Boolean>()
    val showProgressBodyFat = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var bmiResponse = MutableLiveData<BmiResponseModel>()
    var idealWeightResponse = MutableLiveData<IdealWeightResponseModel>()
    var dailyCaloriesResponse = MutableLiveData<DailyCalorieRequirementsResponseModel>()
    var bodyFatResponse = MutableLiveData<BodyFatPercentageResponseModel>()


    fun bmiApiResponse(age: String?, weight: String?, height: String?) {
        showProgressBmi.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.RAPIDAPI).create(HomeService::class.java)
        val call = client.callBmiApi(age, weight, height)
        call.enqueue(object : Callback<BmiResponseModel> {
            override fun onResponse(
                call: Call<BmiResponseModel>, response: Response<BmiResponseModel>
            ) {
                showProgressBmi.postValue(false)
                val body = response.body()
                Log.d("BMIRESPONSE", body.toString())
                if (response.isSuccessful) {
                    bmiResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<BmiResponseModel>, t: Throwable) {
                Log.d("BMIRESPONSE", t.message.toString())

                showProgressBmi.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }


    fun idealWeightApiResponse(gender: String?, height: String?) {
        showProgressIdealWeight.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.RAPIDAPI).create(HomeService::class.java)
        val call = client.callIdealWeightApi(gender, height)
        call.enqueue(object : Callback<IdealWeightResponseModel> {
            override fun onResponse(
                call: Call<IdealWeightResponseModel>, response: Response<IdealWeightResponseModel>
            ) {
                showProgressIdealWeight.postValue(false)
                val body = response.body()
                Log.d("BMIRESPONSE", body.toString())
                if (response.isSuccessful) {
                    idealWeightResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<IdealWeightResponseModel>, t: Throwable) {
                Log.d("BMIRESPONSE", t.message.toString())

                showProgressIdealWeight.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }

    fun dailyCaloriesApiResponse(
        age: String?, gender: String?, height: String?, weight: String?, activity_level: String?
    ) {
        showProgressDalyCalories.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.RAPIDAPI).create(HomeService::class.java)
        val call =
            client.callDailyCalorieRequirementsApi(age, gender, height, weight, activity_level)
        call.enqueue(object : Callback<DailyCalorieRequirementsResponseModel> {
            override fun onResponse(
                call: Call<DailyCalorieRequirementsResponseModel>,
                response: Response<DailyCalorieRequirementsResponseModel>
            ) {
                showProgressDalyCalories.postValue(false)
                val body = response.body()
                Log.d("BMIRESPONSE", body.toString())
                if (response.isSuccessful) {
                    dailyCaloriesResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(
                call: Call<DailyCalorieRequirementsResponseModel>, t: Throwable
            ) {
                Log.d("BMIRESPONSE", t.message.toString())

                showProgressDalyCalories.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })
    }

    fun bodyFatApiResponse(
        age: String?,
        gender: String?,
        weight: String?,
        height: String?,
        neck: String?,
        waist: String?,
        hip: String?
    ) {
        showProgressBodyFat.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.RAPIDAPI).create(HomeService::class.java)
        val call = client.callBodyFatPercentageApi(age, gender, weight, height, neck, waist, hip)
        call.enqueue(object : Callback<BodyFatPercentageResponseModel> {
            override fun onResponse(
                call: Call<BodyFatPercentageResponseModel>,
                response: Response<BodyFatPercentageResponseModel>
            ) {
                showProgressBodyFat.postValue(false)
                val body = response.body()
                Log.d("BMIRESPONSE", body.toString())
                if (response.isSuccessful) {
                    bodyFatResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<BodyFatPercentageResponseModel>, t: Throwable) {
                Log.d("BMIRESPONSE", t.message.toString())

                showProgressBodyFat.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }
}