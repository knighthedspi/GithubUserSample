package com.example.githubusersample.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.githubusersample.data.api.internal.GithubUsersAPI
import com.example.githubusersample.data.db.GithubUserDatabase
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import com.example.githubusersample.data.db.entity.User
import com.example.githubusersample.utils.MainCoroutineRule
import com.example.githubusersample.utils.users
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class GithubUserRemoteMediatorTest {
    @get:Rule
    var rule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @Mock
    lateinit var usersAPI: GithubUsersAPI

    private lateinit var database: GithubUserDatabase
    private lateinit var userDao: UserDao
    private lateinit var userKeyDao: UserKeyDao

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, GithubUserDatabase::class.java).build()
        userDao = database.userDao()
        userKeyDao = database.usersKeyDao()
    }

    @After
    fun close() {
        database.clearAllTables()
        database.close()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        Mockito.`when`(
            usersAPI.getUsers(1)
        ).thenReturn(users)
        val remoteMediator = GithubUserRemoteMediator(
            usersAPI,
            userDao,
            userKeyDao
        )
        val pagingState = PagingState<Int, User>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success, equalTo(true))
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(false))
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        Mockito.`when`(
            usersAPI.getUsers(1)
        ).thenReturn(listOf())
        val remoteMediator = GithubUserRemoteMediator(
            usersAPI,
            userDao,
            userKeyDao
        )
        val pagingState = PagingState<Int, User>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Success, equalTo(true))
        assertThat((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached, equalTo(true))
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        Mockito.`when`(
            usersAPI.getUsers(1)
        ).thenThrow(RuntimeException())
        val remoteMediator = GithubUserRemoteMediator(
            usersAPI,
            userDao,
            userKeyDao
        )
        val pagingState = PagingState<Int, User>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertThat(result is RemoteMediator.MediatorResult.Error, equalTo(true))
    }
}