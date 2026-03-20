package com.anniyam.mlkit_opencv_realtime_attendance.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // ➕ Insert user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // 📋 Get all users
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    // 🔄 Update full user
    @Update
    suspend fun updateUser(user: UserEntity)

    // ☑️ Update attendance only
    @Query("UPDATE users SET isPresent = :isPresent WHERE id = :userId")
    suspend fun updateAttendance(userId: Int, isPresent: Boolean)

    // 🖼️ Update image
    @Query("UPDATE users SET image = :imagePath WHERE id = :userId")
    suspend fun updateUserImage(userId: Int, imagePath: String)

    // ❌ Delete user
    @Delete
    suspend fun deleteUser(user: UserEntity)

    // 🔍 Get single user
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?
}