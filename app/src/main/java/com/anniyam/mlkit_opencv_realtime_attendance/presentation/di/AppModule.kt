package com.anniyam.mlkit_opencv_realtime_attendance.presentation.di

import android.content.Context
import androidx.room.Room
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.AppDatabase
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.dao.UserDao
import com.anniyam.mlkit_opencv_realtime_attendance.data.repository.FaceRepositoryImpl
import com.anniyam.mlkit_opencv_realtime_attendance.data.repository.UserRepositoryImpl
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.FaceRepository
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "attendance_db"
        ).build()
    }

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao {
        return db.userDao()
    }
    @Provides
    @Singleton
    fun provideUserRepository(
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(userDao)
    }
    @Provides
    @Singleton
    fun provideFaceDetectionRepository(
    ): FaceRepository {
        return FaceRepositoryImpl()
    }
}