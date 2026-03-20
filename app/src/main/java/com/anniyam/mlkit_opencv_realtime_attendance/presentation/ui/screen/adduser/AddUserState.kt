package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser

import android.net.Uri

data class AddUserState(
    val name: String = "",
    val phone: String = "",
    val age: String = "",
    val byteArray: ByteArray?= null
)