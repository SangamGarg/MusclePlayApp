package com.sangam.muscleplay.excercises.respository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.muscleplay.AppUtils.AppUrls
import com.example.muscleplay.Retrofit.RetrofitUtilClass
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityResponseModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.network.BurnedCaloriesFromActivityService
import com.sangam.muscleplay.excercises.model.ExerciseResponseModel
import com.sangam.muscleplay.excercises.network.ExerciseNetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseRepository {

    val showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var exerciseResponse = MutableLiveData<ExerciseResponseModel>()
    fun exerciseApiResponse(
        name: String?, muscle: String?, type: String?, difficulty: String?
    ) {
        showProgress.value = true
        val client = RetrofitUtilClass.getRetrofit(AppUrls.EXERCISE)
            .create(ExerciseNetworkService::class.java)
        val call = client.callExcerciseApi(type, name, muscle, difficulty)
        call.enqueue(object : Callback<ExerciseResponseModel> {
            override fun onResponse(
                call: Call<ExerciseResponseModel>, response: Response<ExerciseResponseModel>
            ) {
                Log.d("exerciseResponse", response.toString())
                showProgress.postValue(false)
                val body = response.body()
                Log.d("exerciseResponse", body.toString())
                if (response.isSuccessful) {
                    exerciseResponse.postValue(body!!)
                } else {
                    errorMessage.postValue(response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<ExerciseResponseModel>, t: Throwable) {
                Log.d("exerciseResponse", t.message.toString())
                showProgress.postValue(false)
                errorMessage.postValue("Server error please try after sometime")
            }

        })

    }

}



