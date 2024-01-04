package com.sangam.muscleplay.botton_nav.home.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.botton_nav.home.model.IdealWeightResponseModel
import com.sangam.muscleplay.botton_nav.home.network.HomeService
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
        val client = RetrofitUtilClass.getRetrofit("https://fitness-calculator.p.rapidapi.com/")
            .create(HomeService::class.java)
        var call = client?.callBmiApi(age, weight, height)
        call?.enqueue(object : Callback<BmiResponseModel> {
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
                showProgressBmi.postValue(false)
                errorMessage.postValue("BMI: Server error please try after sometime")
            }

        })

    }


    fun idealWeightApiResponse(gender: String?, height: String?) {
        showProgressIdealWeight.value = true
        val client = RetrofitUtilClass.getRetrofit("https://fitness-calculator.p.rapidapi.com/")
            .create(HomeService::class.java)
        var call = client?.callIdealWeightApi(gender, height)
        call?.enqueue(object : Callback<IdealWeightResponseModel> {
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
                showProgressIdealWeight.postValue(false)
                errorMessage.postValue("Ideal Weight: Server error please try after sometime")
            }

        })

    }

    fun dailyCaloriesApiResponse(
        age: String?, gender: String?, height: String?, weight: String?, activity_level: String?
    ) {
        showProgressDalyCalories.value = true
        val client = RetrofitUtilClass.getRetrofit("https://fitness-calculator.p.rapidapi.com/")
            .create(HomeService::class.java)
        var call =
            client?.callDailyCalorieRequirementsApi(age, gender, height, weight, activity_level)
        call?.enqueue(object : Callback<DailyCalorieRequirementsResponseModel> {
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
                errorMessage.postValue("DailyCalories: Server error please try after sometime")
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
        val client = RetrofitUtilClass.getRetrofit("https://fitness-calculator.p.rapidapi.com/")
            .create(HomeService::class.java)
        var call = client?.callBodyFatPercentageApi(age, gender, weight, height, neck, waist, hip)
        call?.enqueue(object : Callback<BodyFatPercentageResponseModel> {
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
                showProgressBodyFat.postValue(false)
                errorMessage.postValue("BodyFatPer: Server error please try after sometime")
            }

        })

    }
}