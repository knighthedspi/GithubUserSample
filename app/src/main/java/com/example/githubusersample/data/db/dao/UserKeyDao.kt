package com.example.githubusersample.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusersample.data.db.entity.UserKey

@Dao
interface UserKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: List<UserKey>)

    @Query("SELECT * FROM user_key WHERE login = :userId")
    suspend fun userKeyId(userId: String): UserKey?

    @Query("DELETE FROM user_key")
    suspend fun clearUserKeys()
}