package com.anniyam.mlkit_opencv_realtime_attendance.domain.model

data class User(
    val id: Int,
    val name: String,
    val phone: String,
    val age: Int,
    var isPresent: Boolean,
    val image: ByteArray

)
