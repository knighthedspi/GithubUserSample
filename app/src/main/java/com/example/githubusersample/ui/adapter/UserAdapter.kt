package com.example.githubusersample.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.githubusersample.R
import com.example.githubusersample.data.db.entity.User
import com.example.githubusersample.databinding.UserItemBinding
import com.example.githubusersample.ui.view.BindingHolder

class UserAdapter(private val context: Context, private val onUserClick: (User) -> Unit) :
    PagingDataAdapter<User, BindingHolder<UserItemBinding>>(object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.login == newItem.login && oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onBindViewHolder(holder: BindingHolder<UserItemBinding>, position: Int) {
        getItem(position)?.let { user ->
            holder.dataBinding.user = user
            holder.dataBinding.root.setOnClickListener {
                onUserClick.invoke(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<UserItemBinding> {
        return BindingHolder.inflate(
            LayoutInflater.from(context),
            R.layout.user_item,
            parent,
            false
        )
    }
}