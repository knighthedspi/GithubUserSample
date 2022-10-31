package com.example.githubusersample.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.githubusersample.app.InspectorInitializer
import com.facebook.stetho.Stetho
import com.facebook.stetho.timber.StethoTree
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import timber.log.Timber

@InstallIn(SingletonComponent::class)
@Module
object InspectorModule {
    @Provides
    fun inspectorInitializer(): InspectorInitializer {
        return object : InspectorInitializer {
            override fun initialize(application: Application) {
                Stetho.initializeWithDefaults(application)

                Timber.plant(Timber.DebugTree())
                Timber.plant(StethoTree())

                application.registerActivityLifecycleCallbacks(object :
                    Application.ActivityLifecycleCallbacks {
                    override fun onActivityPaused(activity: Activity) {
                        Timber.d("onActivityPaused: ${activity::class.java.simpleName}")
                    }

                    override fun onActivityResumed(activity: Activity) {
                        Timber.d("onActivityResumed: ${activity::class.java.simpleName}")
                    }

                    override fun onActivityStarted(activity: Activity) {
                        Timber.d("onActivityStarted: ${activity::class.java.simpleName}")
                    }

                    override fun onActivityDestroyed(activity: Activity) {
                        Timber.d("onActivityDestroyed: ${activity::class.java.simpleName}")
                    }

                    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                        Timber.d("onActivitySaveInstanceState: ${activity::class.java.simpleName}")
                    }

                    override fun onActivityStopped(activity: Activity) {
                        Timber.d("onActivityStopped: ${activity::class.java.simpleName}")
                    }

                    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                        Timber.d("onActivityCreated: ${activity::class.java.simpleName}")
                    }
                })
            }
        }
    }

}