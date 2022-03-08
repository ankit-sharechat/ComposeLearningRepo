package com.example.composelearning.network

import com.example.composelearning.data.ApiEndpoint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Network {
    fun apiInstance(): ApiEndpoint? {
//        val client = OkHttpClient.Builder().apply {
//            addInterceptor(Interceptor { chain ->
//                val newBuilder = chain.request().newBuilder()
//                newBuilder.header("app-id", "60c1c720a8650ff5e8e0d7cf")
//                return@Interceptor chain.proceed(newBuilder.build())
//            })
//        }.build()

        val retrofit = Retrofit.Builder()
            //.client(client)
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiEndpoint::class.java)
    }
}