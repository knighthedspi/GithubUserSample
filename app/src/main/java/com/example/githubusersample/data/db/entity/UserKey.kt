package com.example.githubusersample.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_key")
data class UserKey(
    @PrimaryKey val login: String,
    @ColumnInfo val prevKey: Int?,
    @ColumnInfo val nextKey: Int?
)