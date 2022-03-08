package com.example.composelearning.data

import com.example.composelearning.network.Network


class DataStore {
    var nextPage = 1
    val network = Network()
    suspend fun getMovieList(): MovieResponse? {
        return network.apiInstance()?.userList(page = nextPage++)
    }
}