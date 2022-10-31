package com.example.githubusersample.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.githubusersample.data.api.internal.GithubUsersAPI
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import com.example.githubusersample.data.db.entity.User
import com.example.githubusersample.data.db.entity.UserKey

@OptIn(ExperimentalPagingApi::class)
class GithubUserRemoteMediator(
    private val usersAPI: GithubUsersAPI,
    private val userDao: UserDao,
    private val userKeyDao: UserKeyDao
) : RemoteMediator<Int, User>() {
    companion object {
        private const val START_ID = 1
        private const val PAGE_SIZE = 30
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        val since = when (loadType) {
            LoadType.REFRESH -> {
                val userKey = getUserKeyClosestToCurrentPosition(state)
                userKey?.nextKey?.minus(PAGE_SIZE) ?: START_ID
            }
            LoadType.PREPEND -> {
                val userKey = getUserKeyForFirstItem(state)
                val prevKey = userKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = userKey != null)
                prevKey
            }
            LoadType.APPEND -> {
                val userKey = getUserKeyForLastItem(state)
                val nextKey = userKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = userKey != null)
                nextKey
            }
        }

        try {
            val users = usersAPI.getUsers(since)
            val endOfPaginationReached = users.isEmpty()
            if (loadType == LoadType.REFRESH) {
                userDao.clearUsers()
                userKeyDao.clearUserKeys()
            }
            val prevKey = if (since == START_ID) null else since - PAGE_SIZE
            val nextKey = if (endOfPaginationReached) null else since + PAGE_SIZE
            val userKeys = users.map {
                UserKey(it.login, prevKey, nextKey)
            }
            users.let {
                userDao.insert(
                    it.map { user ->
                        User(
                            user.login,
                            user.id,
                            user.nodeId,
                            user.avatarUrl,
                            user.gravatarId,
                            user.url,
                            user.htmlUrl,
                            user.followersUrl,
                            user.followingUrl,
                            user.gistsUrl,
                            user.subscriptionsUrl,
                            user.organizationsUrl,
                            user.reposUrl,
                            user.eventsUrl,
                            user.receivedEventsUrl,
                            user.type,
                            user.siteAdmin
                        )
                    }
                )
            }
            userKeys.let {
                userKeyDao.insert(userKeys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getUserKeyForLastItem(state: PagingState<Int, User>): UserKey? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { user ->
                userKeyDao.userKeyId(user.login)
            }
    }

    private suspend fun getUserKeyForFirstItem(state: PagingState<Int, User>): UserKey? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { user ->
                userKeyDao.userKeyId(user.login)
            }
    }

    private suspend fun getUserKeyClosestToCurrentPosition(state: PagingState<Int, User>): UserKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.login?.let { loginId ->
                userKeyDao.userKeyId(loginId)
            }
        }
    }
}