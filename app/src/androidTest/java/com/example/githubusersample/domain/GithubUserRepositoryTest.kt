package com.example.githubusersample.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.githubusersample.data.api.internal.GithubUsersAPI
import com.example.githubusersample.data.db.GithubUserDatabase
import com.example.githubusersample.data.db.dao.UserDao
import com.example.githubusersample.data.db.dao.UserKeyDao
import com.example.githubusersample.utils.MainCoroutineRule
import com.example.githubusersample.utils.userInfo
import com.example.githubusersample.utils.users
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.notNullValue
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
class GithubUserRepositoryTest {
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
    private lateinit var repository: GithubUserRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, GithubUserDatabase::class.java).build()
        userDao = database.userDao()
        userKeyDao = database.usersKeyDao()
        repository = GithubUserRepository(usersAPI, userDao, userKeyDao)
    }

    @After
    fun close() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun testGetUsers() = runBlocking {
        Mockito.`when`(
            usersAPI.getUsers(1)
        ).thenReturn(users)
        val pagingData = repository.getUsers().first()
        assertThat(pagingData, notNullValue())
    }

    @Test
    fun testGetUserInfo() = runBlocking {
        val url = users.first().url
        Mockito.`when`(
            usersAPI.getUserInfo(url)
        ).thenReturn(userInfo)
        val userInfoResult = repository.getUserInfo(url).first()
        assertThat(userInfo, equalTo(userInfoResult))
    }
}