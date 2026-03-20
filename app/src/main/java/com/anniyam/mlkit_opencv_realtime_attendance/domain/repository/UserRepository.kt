package com.anniyam.mlkit_opencv_realtime_attendance.domain.repository

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity.UserEntity
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    suspend fun updateUser(user: User)
}