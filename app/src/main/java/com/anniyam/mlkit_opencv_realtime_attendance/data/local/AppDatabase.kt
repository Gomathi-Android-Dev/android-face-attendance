package com.anniyam.mlkit_opencv_realtime_attendance.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.dao.UserDao
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}