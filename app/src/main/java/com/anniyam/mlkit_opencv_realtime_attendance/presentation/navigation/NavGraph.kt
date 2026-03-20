package com.anniyam.mlkit_opencv_realtime_attendance.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.AddUserScreen
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "user_list"
    ) {
        composable("user_list") { HomeScreen(navController) }
        composable("add_user") { AddUserScreen(navController) }
    }
}