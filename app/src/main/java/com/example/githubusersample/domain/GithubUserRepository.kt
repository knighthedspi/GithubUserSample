package com.example.githubusersample.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubusersample.data.api.internal.GithubUsersAPI
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import com.example.githubusersample.data.db.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GithubUserRepository @Inject constructor(
    private val usersAPI: GithubUsersAPI,
    private val userDao: UserDao,
    private val userKeyDao: UserKeyDao,
) {
    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

    fun getUsers(): Flow<PagingData<User>> {
        val pagingSourceFactory = { userDao.getUsers() }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = GithubUserRemoteMediator(
                usersAPI, userDao, userKeyDao
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getUserInfo(url: String) = flow {
        val userInfo = usersAPI.getUserInfo(url)
        emit(userInfo)
    }
}