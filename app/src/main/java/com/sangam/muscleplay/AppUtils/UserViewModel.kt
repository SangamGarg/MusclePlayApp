package com.sangam.muscleplay.AppUtils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> get() = _userData

    fun setUserData(name: String?, email: String?) {
        _userData.value = UserData(name, email)
    }

    data class UserData(val name: String?, val email: String?)
}