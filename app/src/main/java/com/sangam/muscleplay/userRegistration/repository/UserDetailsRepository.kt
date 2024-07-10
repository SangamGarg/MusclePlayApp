package com.sangam.muscleplay.userRegistration.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sangam.muscleplay.retrofit.RetrofitUtilClass
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.network.UserDetailsNetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserDetailsRepository {

    val showProgress = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val userDetailsResponse = MutableLiveData<UserDetailsResponseModel>()

    fun getUserDetailsResponse(userId: String?) {
        showProgress.postValue(true)
        val client =
            RetrofitUtilClass.getRetrofitMain().create(UserDetailsNetworkService::class.java)
        val call = client.getUserDetails(userId)
        call.enqueue(object : Callback<UserDetailsResponseModel?> {
            override fun onResponse(
                call: Call<UserDetailsResponseModel?>, response: Response<UserDetailsResponseModel?>
            ) {
                showProgress.postValue(false)
                val body = response.body()
                Log.d("userDetailsResponse", "body : ${body.toString()}")

                if (response.isSuccessful) {
                    userDetailsResponse.postValue(body!!)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("userDetailsResponse", "response fail :$errorResponse")
                    when (response.code()) {
                        404 -> errorMessage.postValue("User not exist, please sign up")
                        else -> errorMessage.postValue("Error: $errorResponse")
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailsResponseModel?>, t: Throwable) {
                Log.d("userDetailsResponse", "failed : ${t.localizedMessage}")
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }

    fun postUserDetailsResponse(userDetailsPostRequestBody: UserDetailsPostRequestBody?) {
        showProgress.postValue(true)
        val client =
            RetrofitUtilClass.getRetrofitMain().create(UserDetailsNetworkService::class.java)
        val call = client.postUserDetails(userDetailsPostRequestBody)
        call.enqueue(object : Callback<UserDetailsResponseModel?> {
            override fun onResponse(
                call: Call<UserDetailsResponseModel?>, response: Response<UserDetailsResponseModel?>
            ) {
                showProgress.postValue(false)
                val body = response.body()
                Log.d("userDetailsResponse", "body : ${body.toString()}")

                if (response.isSuccessful) {
                    userDetailsResponse.postValue(body!!)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("userDetailsResponse", "response fail :$errorResponse")
                    when (response.code()) {
                        404 -> errorMessage.postValue("User not exist, please sign up")
                        else -> errorMessage.postValue("Error: $errorResponse")
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailsResponseModel?>, t: Throwable) {
                Log.d("userDetailsResponse", "failed : ${t.localizedMessage}")
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })
    }

    fun putUserDetailsResponse(
        userId: String?, userDetailsResponseModel: UserDetailsResponseModel?
    ) {
        showProgress.postValue(true)
        val client =
            RetrofitUtilClass.getRetrofitMain().create(UserDetailsNetworkService::class.java)
        val call = client.putUserDetails(userId, userDetailsResponseModel)
        call.enqueue(object : Callback<UserDetailsResponseModel?> {
            override fun onResponse(
                call: Call<UserDetailsResponseModel?>, response: Response<UserDetailsResponseModel?>
            ) {
                showProgress.postValue(false)
                val body = response.body()
                Log.d("userDetailsResponse", "body : ${body.toString()}")

                if (response.isSuccessful) {
                    userDetailsResponse.postValue(body!!)
                } else {
                    val errorResponse = response.errorBody()?.string()
                    Log.d("userDetailsResponse", "response fail :$errorResponse")
                    when (response.code()) {
                        404 -> errorMessage.postValue("User not exist, please sign up")
                        else -> errorMessage.postValue("Error: $errorResponse")
                    }
                }
            }

            override fun onFailure(call: Call<UserDetailsResponseModel?>, t: Throwable) {
                Log.d("userDetailsResponse", "failed : ${t.localizedMessage}")
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })
    }


}


