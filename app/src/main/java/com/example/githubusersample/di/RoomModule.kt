package com.example.githubusersample.di

import android.app.Application
import androidx.room.Room
import com.example.githubusersample.data.db.GithubUserDatabase
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {
    @Singleton
    @Provides
    fun database(application: Application): GithubUserDatabase {
        return Room.databaseBuilder(application, GithubUserDatabase::class.java, "githubuser.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun userDao(database: GithubUserDatabase): UserDao {
        return database.userDao()
    }

    @Singleton
    @Provides
    fun usersKeyDao(database: GithubUserDatabase): UserKeyDao {
        return database.usersKeyDao()
    }
}