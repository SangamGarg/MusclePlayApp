package com.sangam.muscleplay.UserDataUtils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.UserDataUtils.model.UserDataExtra
import com.sangam.muscleplay.UserDataUtils.model.UserDataNameAndEmail

class UserViewModel : ViewModel() {
    //    private val _userData = MutableLiveData<UserData>()
//    val userData: LiveData<UserData> get() = _userData
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var userDataResponse = MutableLiveData<UserDataNameAndEmail>()
    var showProgressExtra = MutableLiveData<Boolean>()
    var userDataExtraResponse = MutableLiveData<UserDataExtra>()
    var imageSliderResponse = MutableLiveData<ArrayList<String>>()
    var viewFlipperResponse = MutableLiveData<ArrayList<String>>()
    private val repository = UserDataRepository()

    init {

        this.userDataResponse = repository.userDataResponse
        this.errorMessage = repository.errorMessage
        this.showProgress = repository.showProgress
        this.userDataExtraResponse = repository.userDataExtraResponse
        this.showProgressExtra = repository.showProgressExtra
        this.imageSliderResponse = repository.imageSliderResponse
        this.viewFlipperResponse = repository.viewFlipperResponse
    }

//    fun setUserData(name: String?, email: String?) {
//        _userData.value = UserData(name, email)
//    }

    fun getUserData() {
        repository.getUserData()
    }

    fun getUserDataExtra() {
        repository.getUserDataExtra()
    }

    fun getImageSliderImages() {
        repository.getImageSliderImage()
    }
    fun getViewFlipperImages() {
        repository.getViewFlipperImage()
    }

}