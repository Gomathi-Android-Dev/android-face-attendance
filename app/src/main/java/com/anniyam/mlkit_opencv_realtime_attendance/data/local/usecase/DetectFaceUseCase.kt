package com.anniyam.mlkit_opencv_realtime_attendance.data.local.usecase

import androidx.camera.core.ImageProxy
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.FaceRepository
import javax.inject.Inject

class DetectFaceUseCase @Inject constructor(
    private val repository: FaceRepository
) {
    operator fun invoke(
        imageProxy: ImageProxy,
        onResult: (Boolean) -> Unit
    ) {
        repository.detectFace(imageProxy, onResult)
    }
}