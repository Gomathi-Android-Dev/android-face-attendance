package com.anniyam.mlkit_opencv_realtime_attendance.data.repository

import com.anniyam.mlkit_opencv_realtime_attendance.data.local.dao.UserDao
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity.UserEntity
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.Dispatchers
import com.anniyam.mlkit_opencv_realtime_attendance.data.mapper.toDomain
import com.anniyam.mlkit_opencv_realtime_attendance.data.mapper.toEntity
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao   // ✅ injected here
) : UserRepository {

    override fun getUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
            .map { entities ->
                entities.map { it.toDomain() }
            }
    }

    override suspend fun insertUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    override suspend fun updateUser(user: User) {

        userDao.updateUser(user.toEntity())
    }

}