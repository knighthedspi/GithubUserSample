package com.example.githubusersample.ui.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.example.githubusersample.di.GlideApp

object CustomBindingAdapter {
    @BindingAdapter("imageUrl")
    @JvmStatic
    fun loadImage(view: ImageView, url: String?) {
        if (url == null) {
            return
        }
        GlideApp.with(view)
            .load(url)
            .into(view)
    }
}