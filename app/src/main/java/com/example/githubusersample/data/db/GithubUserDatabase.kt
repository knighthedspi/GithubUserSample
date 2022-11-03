package com.example.githubusersample.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import com.example.githubusersample.data.db.entity.User
import com.example.githubusersample.data.db.entity.UserKey

@Database(
    entities = [
        User::class,
        UserKey::class
    ],
    version = 1
)
abstract class GithubUserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun usersKeyDao(): UserKeyDao
}