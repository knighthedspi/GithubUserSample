package com.example.githubusersample.data.api.model

import com.squareup.moshi.Json

data class UserInfo(
    val login: String,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val email: String?,
    val hireable: Boolean?,
    val bio: String?,
    @Json(name = "avatar_url") val avatarUrl: String?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "public_repos") val publicRepos: Int,
    @Json(name = "public_gists") val publicGists: Int,
    val followers: Int,
    val following: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String
)
