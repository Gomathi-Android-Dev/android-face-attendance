package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.anniyam.mlkit_opencv_realtime_attendance.R
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User

@Composable()
fun HomeScreen(
    navController: NavController,
    viewModel: UserViewModel = hiltViewModel()
) {
    val users by viewModel.userList.collectAsState(initial = emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onAddClick()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // 👈 IMPORTANT (fixes overlap)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.ontakeAttendance()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Take Attendance")
                }
            }
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ Important
                .padding(16.dp)
        ) {

            Text(
                text = "User List",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyColumn {
                items(users.size) { index ->
                    val user = users[index]
                    UserItem(
                        user = user,
                        onToggleAttendance = { isPresent ->
                            // 👈 Call your ViewModel update function here
                            viewModel.updateAttendance(user, isPresent)
                        }
                    )
                }
            }


        }
    }
}

@Composable
fun UserItem(user: User,onToggleAttendance: (Boolean) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 👤 Profile Image
            if (user.image != null) {
                // 📸 Show captured byte array image using Coil
                AsyncImage(
                    model = user.image,
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // 👤 Fallback to default icon if no image exists
                Image(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Default Profile",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 📝 User Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = user.name, fontWeight = FontWeight.Bold)
                Text(text = user.phone)
                Text(text = "Age: ${user.age}")
            }

            // ☑️ Checkbox
            Checkbox(
                checked = user.isPresent,
                onCheckedChange = { onToggleAttendance(it) }
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {

}