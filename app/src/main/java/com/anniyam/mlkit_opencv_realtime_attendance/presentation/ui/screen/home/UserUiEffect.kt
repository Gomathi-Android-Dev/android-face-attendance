package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home

import android.graphics.Bitmap

sealed class UserUiEffect {
    object NavigateToAddUser : UserUiEffect()

    object NavigateToAttendance : UserUiEffect()
}