package com.anniyam.mlkit_opencv_realtime_attendance.domain.repository

import androidx.camera.core.ImageProxy

interface FaceRepository {
    fun detectFace(
        imageProxy: ImageProxy,
        onResult: (Boolean) -> Unit
    )
}