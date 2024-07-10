package com.sangam.muscleplay.retrofit

import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitUtilClass {

    companion object {
        private lateinit var retrofit: Retrofit
        private lateinit var retrofitMain: Retrofit

        fun getRetrofitMain(): Retrofit {
            if (!Companion::retrofitMain.isInitialized) {
                val authInterceptor = Interceptor { chain ->
                    val user = FirebaseAuth.getInstance().currentUser
                    val request = chain.request().newBuilder()
                    user?.let {
                        // Add the UID as a header to the request
                        request.addHeader("uid", "")
                    }
                    chain.proceed(request.build())
                }
                retrofitMain = Retrofit.Builder().baseUrl(BuildConfig.MAIN_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(
                        OkHttpClient.Builder().connectTimeout(180, TimeUnit.SECONDS)
                            .readTimeout(180, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS)
//                            .addInterceptor(authInterceptor) // Add the auth interceptor
                            .addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }).build()
                    ).build()

                return retrofitMain
            }
            return retrofitMain
        }


        fun getRetrofit(baseUrl: String): Retrofit {

            if (!Companion::retrofit.isInitialized) {

                val authInterceptor = Interceptor { chain ->
                    val user = FirebaseAuth.getInstance().currentUser
                    val request = chain.request().newBuilder()
                    user?.let {
                        // Add the UID as a header to the request
                        request.addHeader("uid", "")
                    }
                    chain.proceed(request.build())
                }

                retrofit = Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).client(
                        OkHttpClient.Builder().connectTimeout(180, TimeUnit.SECONDS)
                            .readTimeout(180, TimeUnit.SECONDS).writeTimeout(180, TimeUnit.SECONDS)
//                            .addInterceptor(authInterceptor) // Add the auth interceptor
                            .addInterceptor(HttpLoggingInterceptor().apply {
                                level = HttpLoggingInterceptor.Level.BODY
                            }).build()
                    ).build()

                return retrofit
            }
            return retrofit
        }
    }
}
