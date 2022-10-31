package com.example.githubusersample.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusersample.data.db.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(users: List<User>)

    @Query("SELECT * FROM user")
    fun getUsers(): PagingSource<Int, User>

    @Query("DELETE FROM user")
    suspend fun clearUsers()
}