package com.example.githubusersample.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubusersample.domain.GithubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GithubUserViewModel @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) : ViewModel() {
    val users = githubUserRepository.getUsers()
}