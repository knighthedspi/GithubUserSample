package com.example.githubusersample.data.api.internal

import com.example.githubusersample.data.api.model.User
import com.example.githubusersample.data.api.model.UserInfo
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface GithubUsersAPI {
    @GET("users")
    suspend fun getUsers(@Query("since") since: Int?): List<User>

    @GET
    suspend fun getUserInfo(@Url url: String): UserInfo
}