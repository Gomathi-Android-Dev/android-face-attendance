package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun HomeScreenRoute(navController: NavHostController,
                    viewModel: UserViewModel = hiltViewModel()){

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    HomeScreen(navController = navController)

    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                 is UserUiEffect.NavigateToAddUser ->{
                     navController.navigate("add_user")
                 }

                UserUiEffect.NavigateToAttendance ->  {
                    navController.navigate("take_attendance")
                }
            }
        }
    }
}