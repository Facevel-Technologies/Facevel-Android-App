package com.facevel.inc.app.repository

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class UserRepository @Inject constructor(
    @ApplicationContext val context: Context
) {


    private val _userDataState = MutableStateFlow<UserDataLoadingState>(UserDataLoadingState.Empty)
    val userDataState: StateFlow<UserDataLoadingState> = _userDataState


    fun isLoggedIn(): Boolean {
        return Firebase.auth.currentUser != null
    }

    private fun getUserId(): String? = Firebase.auth.currentUser?.uid

    fun getUserData() {
        val uid = getUserId()

        if (uid != null) {
            println()
            Firebase.firestore.collection("employee").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject<EmployeeData>()
                    println("User Data ---")
                    println(userData)
                    if (userData != null) {
                        _userDataState.value = UserDataLoadingState.Success(userData)
                    } else {
                        _userDataState.value = UserDataLoadingState.Error("Data Empty")
                    }
                }
                .addOnFailureListener {
                    _userDataState.value = UserDataLoadingState.Error("Fetching Error")
                }
        }

    }
}

sealed class UserDataLoadingState {
    object Empty : UserDataLoadingState()
    data class Error(var message: String) : UserDataLoadingState()
    data class Success(var donationCategoryData: EmployeeData) : UserDataLoadingState()
}

/// employee/XLiR2xTEkeU5MuuYp6vJ7q3XnjC3
data class EmployeeData(
    var employee_id: String,
    var department: String,
    var email: String,
    var joining_date: com.google.firebase.Timestamp?,
    var phone_number: String,
    var name: String,
    var profile_photo: String
) {
    constructor() : this("", "","", null, "", "", "", )
}