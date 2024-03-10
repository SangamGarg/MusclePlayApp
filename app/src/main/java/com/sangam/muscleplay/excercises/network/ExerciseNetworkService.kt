package com.sangam.muscleplay.excercises.network

import com.sangam.muscleplay.excercises.model.ExerciseResponseModel
import com.sangam.muscleplay.excercises.model.ExerciseResponseModelItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ExerciseNetworkService {
    @Headers("X-Api-Key: b+iPwAySewJoY0fQMO1A4w==7jd2HY04eYkJxypI")
    @GET("exercises")
    fun callExcerciseApi(
        @Query("type") type: String?,
        @Query("name") name: String?,
        @Query("muscle") muscle: String?,
        @Query("difficulty") difficulty: String?
    ): Call<ExerciseResponseModel>
}