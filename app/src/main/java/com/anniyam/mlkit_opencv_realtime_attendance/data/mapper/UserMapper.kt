package com.anniyam.mlkit_opencv_realtime_attendance.data.mapper

import com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity.UserEntity
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        name = name,
        phone = phone,
        age = age,
        isPresent = isPresent,
        image =  image
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        phone = phone,
        age = age,
        isPresent = isPresent,
        image = image
    )
}