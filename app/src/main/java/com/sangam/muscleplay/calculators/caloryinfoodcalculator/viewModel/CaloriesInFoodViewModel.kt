package com.sangam.muscleplay.calculators.caloryinfoodcalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.calculators.caloryinfoodcalculator.model.CaloriesInFoodResponseModel
import com.sangam.muscleplay.calculators.caloryinfoodcalculator.repository.CaloriesInFoodRepository


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