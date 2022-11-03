package com.example.githubusersample.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val login: String,
    @ColumnInfo val id: Int,
    @ColumnInfo val nodeId: String,
    @ColumnInfo val avatarUrl: String,
    @ColumnInfo val gravatarId: String?,
    @ColumnInfo val url: String,
    @ColumnInfo val htmlUrl: String,
    @ColumnInfo val followersUrl: String,
    @ColumnInfo val followingUrl: String,
    @ColumnInfo val gistsUrl: String,
    @ColumnInfo val starredUrl: String,
    @ColumnInfo val subscriptionsUrl: String,
    @ColumnInfo val organizationsUrl: String,
    @ColumnInfo val reposUrl: String,
    @ColumnInfo val eventsUrl: String,
    @ColumnInfo val receivedEventsUrl: String,
    @ColumnInfo val type: String,
    @ColumnInfo val siteAdmin: Boolean
)