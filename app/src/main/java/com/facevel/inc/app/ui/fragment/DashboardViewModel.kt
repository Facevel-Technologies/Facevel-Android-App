package com.facevel.inc.app.ui.fragment

import androidx.lifecycle.ViewModel
import com.facevel.inc.app.repository.AuthRepository
import com.facevel.inc.app.repository.EmployeeData
import com.facevel.inc.app.repository.UserDataLoadingState
import com.facevel.inc.app.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DashboardViewModel
@Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    fun getUserDataState(): StateFlow<UserDataLoadingState> {
        userRepository.getUserData()
        return userRepository.userDataState
    }

    fun logUserOut() {
        authRepository.signOutUser()
    }
}