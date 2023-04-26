package com.example.tawk.network.service

import com.example.tawk.data.GithubUser
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface UsersServices {

    @GET("users")
    suspend fun getUsersByPage(@Query("since") since: Int): List<GithubUser>

    @GET("users/{username}")
    suspend fun getGithubUserDetails(@Path("username") username: String): GithubUser
}