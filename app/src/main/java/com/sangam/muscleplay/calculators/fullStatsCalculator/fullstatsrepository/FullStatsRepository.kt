package com.sangam.muscleplay.calculators.fullStatsCalculator.fullstatsrepository

import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.sangam.muscleplay.calculators.fullStatsCalculator.network.FullStatsNetworkService
import com.sangam.muscleplay.calculators.model.BmiResponseModel
import com.sangam.muscleplay.calculators.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.calculators.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.calculators.model.IdealWeightResponseModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FullStatsRepository {
    val showProgressBmi = MutableLiveData<Boolean>()
    val showProgressIdealWeight = MutableLiveData<Boolean>()
    val showProgressDalyCalories = MutableLiveData<Boolean>()
    val showProgressBodyFat = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var bmiResponse = MutableLiveData<BmiResponseModel>()
    var idealWeightResponse = MutableLiveData<IdealWeightResponseModel>()
    var dailyCaloriesResponse = MutableLiveData<DailyCalorieRequirementsResponseModel>()
    var bodyFatResponse = MutableLiveData<BodyFatPercentageResponseModel>()


    suspend fun bmiApiResponse(age: String?, weight: String?, height: String?) {
        showProgressBmi.value = true
        try {
            val client = Retrofit.Builder().baseUrl(AppUrls.RAPIDAPI)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FullStatsNetworkService::class.java)
            val response = client.callBmiApi(age, weight, height)
            if (response.isSuccessful) {
                bmiResponse.postValue(response.body()!!)
            } else {
                errorMessage.postValue(response.errorBody().toString())
            }
        } catch (e: Exception) {
            errorMessage.postValue("Server error please try after sometime")
        } finally {
            showProgressBmi.postValue(false)
        }
    }

    suspend fun idealWeightApiResponse(gender: String?, height: String?) {
        showProgressIdealWeight.value = true
        try {
            val client = Retrofit.Builder().baseUrl(AppUrls.RAPIDAPI)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FullStatsNetworkService::class.java)
            val response = client.callIdealWeightApi(gender, height)
            if (response.isSuccessful) {
                idealWeightResponse.postValue(response.body()!!)
            } else {
                errorMessage.postValue(response.errorBody().toString())
            }
        } catch (e: Exception) {
            errorMessage.postValue("Server error please try after sometime")
        } finally {
            showProgressIdealWeight.postValue(false)
        }
    }

    suspend fun dailyCaloriesApiResponse(
        age: String?, gender: String?, height: String?, weight: String?, activity_level: String?
    ) {
        showProgressDalyCalories.value = true
        try {
            val client = Retrofit.Builder().baseUrl(AppUrls.RAPIDAPI)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FullStatsNetworkService::class.java)
            val response =
                client.callDailyCalorieRequirementsApi(age, gender, height, weight, activity_level)
            if (response.isSuccessful) {
                dailyCaloriesResponse.postValue(response.body()!!)
            } else {
                errorMessage.postValue(response.errorBody().toString())
            }
        } catch (e: Exception) {
            errorMessage.postValue("Server error please try after sometime")
        } finally {
            showProgressDalyCalories.postValue(false)
        }
    }

    suspend fun bodyFatApiResponse(
        age: String?,
        gender: String?,
        weight: String?,
        height: String?,
        neck: String?,
        waist: String?,
        hip: String?
    ) {
        showProgressBodyFat.value = true
        try {
            val client = Retrofit.Builder().baseUrl(AppUrls.RAPIDAPI)
                .addConverterFactory(GsonConverterFactory.create()).build()
                .create(FullStatsNetworkService::class.java)
            val response =
                client.callBodyFatPercentageApi(age, gender, weight, height, neck, waist, hip)
            if (response.isSuccessful) {
                bodyFatResponse.postValue(response.body()!!)
            } else {
                errorMessage.postValue(response.errorBody().toString())
            }
        } catch (e: Exception) {
            errorMessage.postValue("Server error please try after sometime")
        } finally {
            showProgressBodyFat.postValue(false)
        }
    }

}