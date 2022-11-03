package com.example.githubusersample.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.githubusersample.R
import com.example.githubusersample.databinding.LoadStateViewItemBinding
import com.example.githubusersample.ui.view.BindingHolder

class LoadStateAdapter(
    private val context: Context,
    private val retry: () -> Unit
) : LoadStateAdapter<BindingHolder<LoadStateViewItemBinding>>() {
    override fun onBindViewHolder(
        holder: BindingHolder<LoadStateViewItemBinding>,
        loadState: LoadState
    ) {
        holder.dataBinding.apply {
            this.loadState = loadState
            this.retryButton.setOnClickListener { retry.invoke() }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BindingHolder<LoadStateViewItemBinding> {
        return BindingHolder.inflate(
            LayoutInflater.from(context),
            R.layout.load_state_view_item,
            parent,
            false
        )
    }
}