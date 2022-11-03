package com.example.githubusersample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    private const val CONNECT_TIME_OUT = 30
    private const val READ_TIME_OUT = 30
    private const val WRITE_TIME_OUT = 30

    @Provides
    fun okHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT.toLong(), TimeUnit.SECONDS)
        return builder.build()
    }
}