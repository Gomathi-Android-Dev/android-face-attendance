package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.attendance

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.CameraScreen
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.CameraUIEffect
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.CameraViewModel
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun AttendanceScreen(navController: NavController,
                     cameraViewModel: CameraViewModel = hiltViewModel(),
                     userViewModel : UserViewModel = hiltViewModel()
){
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val users by userViewModel.userList.collectAsState(initial = emptyList())
    var isCameraActive by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        cameraViewModel.uiEffect.collect { effect ->
            if (effect is CameraUIEffect.ShowCapturedImage) {
                isCameraActive = false
                capturedBitmap = effect.bitmap
                Log.e("Captured","Success")
                withContext(Dispatchers.Default) {
                    var bestMatch: User? = null
                    var maxSimilarity = 0f

                    users.forEach { user ->
                        val storedBitmap = byteArrayToBitmap(user.image)
                        if (storedBitmap != null) {
                            // 💡 Use the Cosine Similarity function
                            val similarity = compareBitmapsCosine(capturedBitmap!!, storedBitmap)
                            Log.d("Similarity", "${user.name}: $similarity")

                            // For pixel vectors, a very high threshold (0.95+) is usually needed
                            if (similarity > 0.92f && similarity > maxSimilarity) {
                                maxSimilarity = similarity
                                bestMatch = user
                            }
                        }
                    }

                    if (bestMatch != null) {
                        withContext(Dispatchers.Main) {
                            userViewModel.updateAttendance(bestMatch!!, true)
                            Log.d("MATCH", "Verified: ${bestMatch!!.name}")
                             navController.popBackStack() // Optional: auto close on success
                        }
                    }
                }
                 // e.g., faceRecognizer.recognize(capturedBitmap)
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Take Attendance",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Reuse your existing CameraScreen component
        Box(
            modifier = Modifier
                .padding(16.dp)
                .size(width = 240.dp, height = 320.dp) // Standard Passport Aspect Ratio (3:4)
                .clip(RoundedCornerShape(16.dp))      // Rounded corners
                .border(
                    2.dp, MaterialTheme.colorScheme.primary,
                    androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                ) // Frame border
        ) {
            if (isCameraActive) {
                CameraScreen(
                    viewModel = cameraViewModel,
                    buttonText = "Capture & Recognize"
                )
            } else {
                // 4. Show a placeholder or the captured image while processing
                capturedBitmap?.let {
                    androidx.compose.foundation.Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Captured Frame",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        if (capturedBitmap != null) {
            Text(
                text = "Face Captured! Processing...",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}



fun compareBitmapsCosine(b1: Bitmap, b2: Bitmap): Float {
    val size = 100
    val resized1 = Bitmap.createScaledBitmap(b1, size, size, true)
    val resized2 = Bitmap.createScaledBitmap(b2, size, size, true)

    var dotProduct = 0.0
    var normA = 0.0
    var normB = 0.0

    for (y in 0 until size) {
        for (x in 0 until size) {
            val p1 = resized1.getPixel(x, y)
            val p2 = resized2.getPixel(x, y)

            // Extract Grayscale/Luminance to treat as a single vector component
            // (Comparing 3D RGB vectors via Cosine is more complex,
            // grayscale is standard for basic pixel vector math)
            val v1 = (0.299 * (p1 shr 16 and 0xff) + 0.587 * (p1 shr 8 and 0xff) + 0.114 * (p1 and 0xff))
            val v2 = (0.299 * (p2 shr 16 and 0xff) + 0.587 * (p2 shr 8 and 0xff) + 0.114 * (p2 and 0xff))

            dotProduct += v1 * v2
            normA += v1.pow(2)
            normB += v2.pow(2)
        }
    }

    val similarity = dotProduct / (sqrt(normA) * sqrt(normB))
    return similarity.toFloat()
}

fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
    return byteArray?.let {
        BitmapFactory.decodeByteArray(it, 0, it.size)
    }
}