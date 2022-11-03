package com.example.githubusersample.di

import com.example.githubusersample.data.api.internal.GithubUsersAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    fun retrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .callFactory(okHttpClient)
            .build()
    }

    @Provides
    fun githubUserAPI(retrofit: Retrofit): GithubUsersAPI {
        return retrofit.create(GithubUsersAPI::class.java)
    }

    @Provides
    fun dispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}