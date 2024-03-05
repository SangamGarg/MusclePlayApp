package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.repository.CaloriesInFoodRepository
import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.botton_nav.home.model.IdealWeightResponseModel
import com.sangam.muscleplay.botton_nav.home.repository.HomeRepository

class CaloriesInFoodViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var caloriesInFoodResponse = MutableLiveData<CaloriesInFoodResponseModel>()
    private val repository = CaloriesInFoodRepository()

    init {
        this.caloriesInFoodResponse = repository.caloriesInFoodResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callCaloriesInFoodApi(query: String?) {
        repository.caloriesInFoodApiResponse(query)
    }
}