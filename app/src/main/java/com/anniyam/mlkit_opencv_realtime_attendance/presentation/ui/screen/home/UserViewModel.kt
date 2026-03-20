package com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anniyam.mlkit_opencv_realtime_attendance.data.mapper.toEntity
import com.anniyam.mlkit_opencv_realtime_attendance.domain.model.User
import com.anniyam.mlkit_opencv_realtime_attendance.domain.repository.UserRepository
import com.anniyam.mlkit_opencv_realtime_attendance.presentation.ui.screen.adduser.AddUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiEffect = MutableSharedFlow<UserUiEffect>()
    val uiEffect = _uiEffect.asSharedFlow()

    private val _state = MutableStateFlow(AddUserState())
    val state = _state.asStateFlow()

    fun onAddClick() {
        viewModelScope.launch {
            _uiEffect.emit(UserUiEffect.NavigateToAddUser)
        }
    }
    fun ontakeAttendance() {
        viewModelScope.launch {
            _uiEffect.emit(UserUiEffect.NavigateToAttendance)
        }
    }
    fun onNameChange(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun onPhoneChange(value: String) {
        _state.update { it.copy(phone = value) }
    }

    fun onAgeChange(value: String) {
        _state.update { it.copy(age = value) }
    }
    fun onImageCaptured(bytes: ByteArray) {
        _state.update { it.copy(byteArray = bytes) }
    }


    fun saveUser(context: Context) {
        viewModelScope.launch {


           val user = state.value.byteArray?.let {
               User(
                   id = 0,
                   name = state.value.name,
                   phone = state.value.phone,
                   age = state.value.age.toIntOrNull() ?: 0,
                   isPresent = false,
                   image = it
               )
           }
            repository.insertUser(user!!.toEntity())
        }
    }
    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    fun updateAttendance(user: User, isPresent: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Create a copy of the user with the new status
                val updatedUser = user.copy(isPresent = isPresent)

                // 2. Update in database via repository
                repository.updateUser(updatedUser)

                Log.d("UserViewModel", "Attendance updated for ${user.name}")
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to update attendance", e)
            }
        }
    }
    val userList: StateFlow<List<User>> =
        repository.getUsers()
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )
}