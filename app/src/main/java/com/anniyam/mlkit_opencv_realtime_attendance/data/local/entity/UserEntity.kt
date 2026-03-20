package com.anniyam.mlkit_opencv_realtime_attendance.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val age: Int,
    val isPresent: Boolean,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray

)