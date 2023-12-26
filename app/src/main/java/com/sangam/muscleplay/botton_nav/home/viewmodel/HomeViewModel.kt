package com.sangam.muscleplay.botton_nav.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.repository.HomeRepository

class HomeViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var bmiResponse = MutableLiveData<BmiResponseModel>()
    private val repository = HomeRepository()

    init {
        this.bmiResponse = repository.bmiResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage

    }

    fun callBmiApi(age: String?, weight: String?, height: String?) {
        repository.bmiApiResponse(age, weight, height)
    }
}