package com.example.muscleplay.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtilClass {
    companion object {
        private lateinit var retrofit: Retrofit

        fun getRetrofit(baseUrl: String) :Retrofit{
            if (!::retrofit.isInitialized) {
                retrofit = Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build()
            }
            return retrofit
        }

    }
}