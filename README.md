# 📸 Real-Time Face Attendance System

An Android application that captures and recognizes faces in real time to automate attendance using **CameraX**, **ML Kit**, and **TensorFlow Lite**.

## 🚀 Features

* 🎥 **Live Camera Preview**

  * Built using CameraX for smooth and lifecycle-aware camera integration

* 🤖 **Face Detection (ML Kit)**

  * Detects faces in real time from camera frames
  * Fast and optimized on-device processing

* 🧠 **Face Recognition (TensorFlow Lite)**

  * Uses a TFLite model to generate face embeddings
  * Matches detected faces with registered users

* 👤 **User Management**

  * Add new users with photo, name, phone, and age
  * Stores user data locally

* ✅ **Attendance System**

  * Automatically marks attendance after successful face recognition
  * Supports multiple users

* 🖼️ **Modern UI**

  * Built with Jetpack Compose
  * Clean and minimal design

## 🏗️ Tech Stack

* **Language:** Kotlin
* **Architecture:** MVVM + Clean Architecture
* **UI:** Jetpack Compose
* **Camera:** CameraX
* **Face Detection:** ML Kit
* **Face Recognition:** TensorFlow Lite
* **Local Storage:** Room Database

## 📱 Screens

* 📷 Camera Preview & Face Recognition
* 👥 User List with Attendance Selection
* ➕ Add User with Image Capture

## ⚙️ How It Works

1. Capture face using CameraX
2. Detect face using ML Kit
3. Convert face to embedding using TensorFlow Lite
4. Compare with stored embeddings
5. Mark attendance if matched

## 📌 Use Cases

* School / College Attendance
* Office Employee Tracking
* Secure Entry Systems

## 🔮 Future Improvements

* Cloud sync (Firebase / Backend API)
* Attendance reports & analytics
* Liveness detection (anti-spoofing)
* Multi-face recognition optimization

---

⭐ If you like this project, give it a star on GitHub!
