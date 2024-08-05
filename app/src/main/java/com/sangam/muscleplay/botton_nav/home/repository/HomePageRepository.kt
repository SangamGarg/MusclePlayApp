package com.sangam.muscleplay.botton_nav.home.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sangam.muscleplay.botton_nav.home.model.HomeRequestBody
import com.sangam.muscleplay.botton_nav.home.model.HomeResponseModel
import com.sangam.muscleplay.botton_nav.home.network.HomeNetworkService
import com.sangam.muscleplay.retrofit.RetrofitUtilClass
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.network.UserDetailsNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageRepository {
    val showProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val homeDetailsResponse = MutableLiveData<HomeResponseModel>()

    fun getHomeDetailsResponse(homeRequestBody: HomeRequestBody?) {
        showProgress.postValue(true)
        val client =
            RetrofitUtilClass.getRetrofitMain().create(HomeNetworkService::class.java)
        val call = client.callHomeApi(homeRequestBody)
        call.enqueue(object : Callback<HomeResponseModel?> {
            override fun onResponse(
                call: Call<HomeResponseModel?>, response: Response<HomeResponseModel?>
            ) {
                showProgress.postValue(false)
                val body = response.body()
                Log.d("homeDetailsResponse", "body : ${body.toString()}")

                if (response.isSuccessful) {
                    homeDetailsResponse.postValue(body!!)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("homeDetailsResponse", "response fail :$errorResponse")
                    when (response.code()) {
                        404 -> errorMessage.postValue("User not exist, please sign up")
                        else -> errorMessage.postValue("Error: $errorResponse")
                    }
                }
            }

            override fun onFailure(call: Call<HomeResponseModel?>, t: Throwable) {
                Log.d("homeDetailsResponse", "failed : ${t.localizedMessage}")
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }
}