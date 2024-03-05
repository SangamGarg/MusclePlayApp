package com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.repository.CaloriesInFoodRepository
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotRequestBodyModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.model.AiChatBotResponseModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_personal_assistant.repository.AiChatBotRepository

class AiChatBotViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var aiChatBotResponse = MutableLiveData<AiChatBotResponseModel>()
    private val repository = AiChatBotRepository()

    init {
        this.aiChatBotResponse = repository.aiChatBotResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callAiChatBotApi(
        authorization: String,
        contentType: String,
        aiChatBotRequestBodyModel: AiChatBotRequestBodyModel
    ) {
        repository.aiChatBotApiResponse(authorization, contentType, aiChatBotRequestBodyModel)
    }
}