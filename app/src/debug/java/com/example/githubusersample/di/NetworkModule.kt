package com.example.githubusersample.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
        builder.addNetworkInterceptor(StethoInterceptor())
        val httpLoggingInterceptor =
            HttpLoggingInterceptor().also {
                it.level = HttpLoggingInterceptor.Level.BODY
            }
        builder.addInterceptor(httpLoggingInterceptor)
        return builder.build()
    }
}