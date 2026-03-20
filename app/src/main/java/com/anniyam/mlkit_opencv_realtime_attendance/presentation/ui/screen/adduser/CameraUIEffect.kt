package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser

import android.graphics.Bitmap

sealed class CameraUIEffect {
    data class ShowCapturedImage(val bitmap: Bitmap) : CameraUIEffect()

}