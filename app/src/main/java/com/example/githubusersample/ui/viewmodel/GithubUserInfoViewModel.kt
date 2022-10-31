package com.example.githubusersample.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubusersample.data.api.model.UserInfo
import com.example.githubusersample.domain.GithubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GithubUserInfoViewModel @Inject constructor(
    private val repository: GithubUserRepository
) : ViewModel() {
    private val _error: MutableLiveData<Unit> = MutableLiveData()
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()

    val errorLiveData: LiveData<Unit> = _error
    val userInfoLiveData: LiveData<UserInfo> =_userInfo

    fun getUserInfo(url: String) = viewModelScope.launch {
        repository.getUserInfo(url)
            .catch {
                _error.postValue(Unit)
            }
            .collect {
                _userInfo.postValue(it)
            }
    }
}