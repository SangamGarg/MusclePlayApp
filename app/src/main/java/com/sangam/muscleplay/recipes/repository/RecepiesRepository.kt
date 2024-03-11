package com.sangam.muscleplay.recipes.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.recipes.model.RecipesResponseModel
import com.sangam.muscleplay.recipes.network.RecipesNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecepiesRepository {
    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var recipesResponse = MutableLiveData<RecipesResponseModel>()
    fun recipesResponse(
        query: String?
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.RECIPE)
            .create(RecipesNetworkService::class.java)
        val call = client.callRecepiesApi(query)
        call.enqueue(object : Callback<RecipesResponseModel> {
            override fun onResponse(
                call: Call<RecipesResponseModel>,
                response: Response<RecipesResponseModel>
            ) {
                Log.d("recepiesResponse", response.toString())
                showProgress.postValue(false)
                val body = response.body()
                Log.d("recepiesResponse", body.toString())
                if (response.isSuccessful) {
                    recipesResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<RecipesResponseModel>, t: Throwable) {
                Log.d("recepiesResponse", t.message.toString())

                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }


}