package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home.UserViewModel
import java.io.File

@Composable
fun AddUserScreen (navController: NavController,
                   viewModel: UserViewModel = hiltViewModel(),
                   cameraViewModel : CameraViewModel = hiltViewModel())
 {

     val state by viewModel.state.collectAsState()
      val context = LocalContext.current
     val capturedBytes by cameraViewModel.capturedImageBytes.collectAsState()
     var showCamera by remember { mutableStateOf(false) }
     LaunchedEffect(capturedBytes) {
         capturedBytes?.let {
             viewModel.onImageCaptured(it)
             showCamera = false
         }
     }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Add User",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Box(
            modifier = Modifier
                .size(150.dp) // Increased size for better visibility
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    showCamera = true // 4. Open camera on click
                },
            contentAlignment = Alignment.Center
        ) {
            if (state.byteArray != null) {
                // Show the captured image if available
                val bitmap = BitmapFactory.decodeByteArray(state.byteArray, 0, state.byteArray!!.size)
                AsyncImage(
                    model = bitmap,
                    contentDescription = "Captured Face",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text("Tap to Take Photo", fontSize = 12.sp, color = Color.DarkGray)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // 5. Show CameraScreen ONLY when showCamera is true
        if (showCamera) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Black)
            ) {
                CameraScreen(viewModel = cameraViewModel,"Capture")

                // Optional: Add a close button
                Button(
                    onClick = { showCamera = false },
                    modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                ) {
                    Text("X")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Name
        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // 🔹 Phone
        OutlinedTextField(
            value = state.phone,
            onValueChange = viewModel::onPhoneChange,
            label = { Text("Contact Number") },
            modifier = Modifier.fillMaxWidth()
        )

        // 🔹 Age
        OutlinedTextField(
            value = state.age,
            onValueChange = viewModel::onAgeChange,
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 🔹 Save Button
        Button(
            onClick = {
                 viewModel.saveUser(context)
                 navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add User")
        }
    }
}

@Composable
fun CameraImagePicker() {  //default camera

    val context = LocalContext.current
    val activity = context as Activity

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // ✅ Check permission
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // ✅ Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // ✅ Create temp file
    val tempFile = remember {
        File.createTempFile("camera_", ".jpg", context.cacheDir)
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tempFile
    )

    // ✅ Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = uri
        }
    }

    // ✅ UI
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
            .clickable {
                if (hasCameraPermission) {

                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA) // 🔐 ask permission
                }
            },
        contentAlignment = Alignment.Center
    ) {

        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text("Take Photo")
        }
    }
}

@Composable
fun CameraScreen(viewModel: CameraViewModel = hiltViewModel(),buttonText: String = "Capture") {   //MLKit camera

    val faceDetected by viewModel.faceDetected.collectAsState()
    val context = LocalContext.current


    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }



    Box  {
        CameraPreview(   // 👈 AndroidView + CameraX
            onFrameAnalyzed = { imageProxy ->
                viewModel.processFrame(imageProxy)
            },
            onImageCaptureReady = {
                imageCapture = it   // ✅ NOW NOT NULL
            }
        )

        if (faceDetected) {
            Button(
                onClick = {

                    imageCapture?.let {
                        viewModel.captureImage(
                            it,
                            ContextCompat.getMainExecutor(context)
                        )
                    }
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(text = buttonText)
            }
        }
    }
}

 @Composable
fun CameraPreview(
    onFrameAnalyzed: (ImageProxy) -> Unit,
    onImageCaptureReady: (ImageCapture) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current   // ✅ FIXED

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->

            val previewView = PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }

            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(
                        ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                    )
                    .build()

                imageAnalyzer.setAnalyzer(
                    ContextCompat.getMainExecutor(ctx)
                ) { imageProxy ->
                    onFrameAnalyzed(imageProxy)
                }

                val imageCapture = ImageCapture.Builder().build()
                onImageCaptureReady(imageCapture)

                val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer,
                    imageCapture
                )

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}