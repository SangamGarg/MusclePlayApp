package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.network.BurnedCaloriesFromActivityService
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BurnedCaloriesFromActivityRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var burnedCaloriesFromActivityResponse = MutableLiveData<BurnedCaloriesFromActivityModel>()
    fun burnedCaloriesFromActivityResponse(
        activity: String?
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.BURNED_CALORIES)
            .create(BurnedCaloriesFromActivityService::class.java)
        var call = client.callBurnedCaloriesFromActivityApi(activity)
        call?.enqueue(object : Callback<BurnedCaloriesFromActivityModel> {
            override fun onResponse(
                call: Call<BurnedCaloriesFromActivityModel>,
                response: Response<BurnedCaloriesFromActivityModel>
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

            override fun onFailure(call: Call<BurnedCaloriesFromActivityModel>, t: Throwable) {
                Log.d("BurnedCaloriesFromActivity", t.message.toString())
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }


}