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
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GithubUserRepository
) : ViewModel() {
    private var _loading: MutableLiveData<Boolean> = MutableLiveData()
    private val _error: MutableLiveData<Unit> = MutableLiveData()
    private val _userInfo: MutableLiveData<UserInfo> = MutableLiveData()

    val users = repository.getUsers()
    val isLoading: LiveData<Boolean> = _loading
    val errorLiveData: LiveData<Unit> = _error
    val userInfoLiveData: LiveData<UserInfo> =_userInfo

    fun getUserInfo(url: String) = viewModelScope.launch {
        _loading.postValue(true)
        repository.getUserInfo(url)
            .catch {
                Timber.e(it)
                _loading.postValue(false)
                _error.postValue(Unit)
            }
            .collect {
                _loading.postValue(false)
                _userInfo.postValue(it)
            }
    }
}