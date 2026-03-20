package com.anniyam.mlkit_opencv_realtime_attendance.data.repository

import androidx.camera.core.ImageProxy
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.FaceRepository
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject

class FaceRepositoryImpl @Inject constructor() : FaceRepository {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .build()
    )

    override fun detectFace(
        imageProxy: ImageProxy,
        onResult: (Boolean) -> Unit
    ) {
        val mediaImage = imageProxy.image

        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            detector.process(image)
                .addOnSuccessListener { faces ->
                    onResult(faces.isNotEmpty())
                }
                .addOnFailureListener {
                    onResult(false)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}