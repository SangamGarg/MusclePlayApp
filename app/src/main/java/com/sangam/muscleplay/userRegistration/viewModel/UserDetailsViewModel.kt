package com.sangam.muscleplay.userRegistration.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.repository.UserDetailsRepository
import kotlinx.coroutines.launch

object UserDetailData {
    var userDetailsResponseModel: UserDetailsResponseModel? = null
}

class UserDetailsViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var userDetailsResponse = MutableLiveData<UserDetailsResponseModel>()
    private val repository = UserDetailsRepository()

    init {
        showProgress = repository.showProgress
        errorMessage = repository.errorMessage
        userDetailsResponse = repository.userDetailsResponse
    }

    fun callGetUserDetails(userId: String?) {
        repository.getUserDetailsResponse(userId)
    }

    fun callPostUserDetails(postUserDetailsPostRequestBodyModel: UserDetailsPostRequestBody?) {
        repository.postUserDetailsResponse(postUserDetailsPostRequestBodyModel)
    }


    fun callPutUserDetails(
        userId: String?, userDetailsResponseModel: UserDetailsResponseModel?
    ) {
        repository.putUserDetailsResponse(userId, userDetailsResponseModel)
    }

    fun setUserDetailsResponse(response: UserDetailsResponseModel) {
        UserDetailData.userDetailsResponseModel = response
    }

    fun getUserDetailsResponse(): UserDetailsResponseModel? {
        return UserDetailData.userDetailsResponseModel
    }
}
