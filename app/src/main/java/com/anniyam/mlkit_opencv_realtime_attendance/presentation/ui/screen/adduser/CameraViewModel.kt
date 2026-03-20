package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniyam.mlkit_opencv_realtime_attendance.data.local.usecase.DetectFaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val detectFaceUseCase: DetectFaceUseCase
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<CameraUIEffect>()
    val uiEffect = _uiEffect.asSharedFlow()
    private val _faceDetected = MutableStateFlow(false)
    val faceDetected: StateFlow<Boolean> = _faceDetected

    // Hold the byte array to pass to the UserViewModel later
    private val _capturedImageBytes = MutableStateFlow<ByteArray?>(null)
    val capturedImageBytes: StateFlow<ByteArray?> = _capturedImageBytes

    fun processFrame(imageProxy: ImageProxy) {
        detectFaceUseCase(imageProxy) { detected ->
            _faceDetected.value = detected
        }
    }


    fun captureImage(
        imageCapture: ImageCapture,
        executor: Executor
    ) {
        takePhoto(imageCapture, executor) { bitmap ->
            val byteData = bitmap.toByteArray()
            _capturedImageBytes.value = byteData

            viewModelScope.launch {
                _uiEffect.emit(CameraUIEffect.ShowCapturedImage(bitmap))
            }
        }
    }
    fun takePhoto(
        imageCapture: ImageCapture,
        executor: Executor,
        onBitmapReady: (Bitmap) -> Unit
    ) {
        imageCapture.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {

                override fun onCaptureSuccess(image: ImageProxy) {
                    val bitmap = imageProxyToBitmap(image)
                    onBitmapReady(bitmap)
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    exception.printStackTrace()
                }
            }
        )
    }
    fun imageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
    private fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
}