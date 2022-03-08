package com.example.composelearning.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("popular?api_key=03fb3218226482e721e53c9ca0069ecd&language=en-US")
    suspend fun userList(@Query("page") page: Int): MovieResponse
}