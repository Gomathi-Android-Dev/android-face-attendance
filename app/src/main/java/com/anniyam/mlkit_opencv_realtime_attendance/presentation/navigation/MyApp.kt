package com.anniyam.mlkit_opencv_realtime_attendance.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.attendance.AttendanceScreen
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.AddUserScreen
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home.HomeScreen
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home.HomeScreenRoute

@Composable
fun MyApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "user_list"
    ) {

        composable("user_list") {
            HomeScreenRoute(navController)
        }

        composable("add_user") {
            AddUserScreen(navController)
        }
        composable("take_attendance") {
            AttendanceScreen(navController)
        }
    }
}

