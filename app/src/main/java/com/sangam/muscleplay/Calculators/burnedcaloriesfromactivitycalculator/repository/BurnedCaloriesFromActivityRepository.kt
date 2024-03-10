package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityResponseModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.network.BurnedCaloriesFromActivityService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BurnedCaloriesFromActivityRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var burnedCaloriesFromActivityResponse = MutableLiveData<BurnedCaloriesFromActivityResponseModel>()
    fun burnedCaloriesFromActivityResponse(
        activity: String?
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.BURNED_CALORIES)
            .create(BurnedCaloriesFromActivityService::class.java)
        val call = client.callBurnedCaloriesFromActivityApi(activity)
        call.enqueue(object : Callback<BurnedCaloriesFromActivityResponseModel> {
            override fun onResponse(
                call: Call<BurnedCaloriesFromActivityResponseModel>,
                response: Response<BurnedCaloriesFromActivityResponseModel>
            ) {
                Log.d("BurnedCaloriesFromActivity", response.toString())
                showProgress.postValue(false)
                val body = response.body()
                Log.d("BurnedCaloriesFromActivity", body.toString())
                if (response.isSuccessful) {
                    burnedCaloriesFromActivityResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<BurnedCaloriesFromActivityResponseModel>, t: Throwable) {
                Log.d("BurnedCaloriesFromActivity", t.message.toString())
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }


}