plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.anniyam.mlkit_opencv_realtime_attendance"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.anniyam.mlkit_opencv_realtime_attendance"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
    }
    kapt {
        correctErrorTypes = true
    }
    configurations.all {
        resolutionStrategy {
            // Force the use of the newer JetBrains annotations to resolve the conflict
            force("org.jetbrains:annotations:23.0.0")

            // Or exclude the older intellij annotations specifically
            exclude(group = "com.intellij", module = "annotations")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "2.1.0" // Must match Compose plugin
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.tooling.preview)
    debugImplementation(libs.compose.tooling)
    implementation(libs.compose.live.data )

    // Dagger Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Coroutines
    implementation(libs.coroutines.android)
    implementation(libs.coroutines.core)
    testImplementation(libs.coroutines.test)
    implementation(libs.coroutines.play.services)


    // Navigation
    implementation(libs.nav.compose)

    // ViewModel Compose
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.javapoet)

    /*Retrofit api's*/
    implementation(libs.retrofit.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit.okhttp)
    implementation(libs.converter.gson)
    implementation(libs.retrofit.gson)

    // Hilt Compose
    implementation(libs.hilt.navigation.compose)

    // Security
    implementation(libs.security.crypto)

    implementation(libs.bouncycastle.bcprov)

    // Location
    implementation(libs.play.services.location)

    //Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)

    /*Room*/
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.kapt)

   /*ML kit*/
    implementation(libs.mlkit.face)

    implementation(libs.camera.core)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)

    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)
}
