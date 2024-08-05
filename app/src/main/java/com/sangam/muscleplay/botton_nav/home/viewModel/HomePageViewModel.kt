package com.sangam.muscleplay.botton_nav.home.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.botton_nav.home.model.HomeRequestBody
import com.sangam.muscleplay.botton_nav.home.model.HomeResponseModel
import com.sangam.muscleplay.botton_nav.home.repository.HomePageRepository
import com.sangam.muscleplay.calculators.repository.HomeRepository
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.repository.UserDetailsRepository
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData


object HomeDetailData {
    var homeDetailsResponse: HomeResponseModel? = null
}

class HomePageViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var homeDetailsResponse = MutableLiveData<HomeResponseModel>()
    private val repository = HomePageRepository()

    init {
        showProgress = repository.showProgress
        errorMessage = repository.errorMessage
        homeDetailsResponse = repository.homeDetailsResponse
    }

    fun callHomeDetails(homeRequestBody: HomeRequestBody?) {
        repository.getHomeDetailsResponse(homeRequestBody)
    }

    fun setHomeDetailsResponse(response: HomeResponseModel) {
        HomeDetailData.homeDetailsResponse = response
    }

    fun getHomeDetailsResponse(): HomeResponseModel? {
        return HomeDetailData.homeDetailsResponse
    }
}