package com.sangam.muscleplay.botton_nav.home.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.network.HomeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var bmiResponse = MutableLiveData<BmiResponseModel>()


    fun bmiApiResponse(age: String?, weight: String?, height: String?) {
        showProgress.value = true
        val client =
            RetrofitUtilClass.getRetrofit("https://fitness-calculator.p.rapidapi.com/")
                .create(HomeService::class.java)
        var call = client?.callBmiApi(age, weight, height)
        call?.enqueue(object : Callback<BmiResponseModel> {
            override fun onResponse(
                call: Call<BmiResponseModel>,
                response: Response<BmiResponseModel>
            ) {
                showProgress.postValue(false)
                val body = response.body()
                Log.d("BMIRESPONSE", body.toString())
                if (response.isSuccessful) {
                    bmiResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<BmiResponseModel>, t: Throwable) {
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }
}