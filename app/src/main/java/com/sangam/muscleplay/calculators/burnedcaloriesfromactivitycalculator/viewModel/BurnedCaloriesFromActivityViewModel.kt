package com.sangam.muscleplay.calculators.burnedcaloriesfromactivitycalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityResponseModel
import com.sangam.muscleplay.calculators.burnedcaloriesfromactivitycalculator.repository.BurnedCaloriesFromActivityRepository

class BurnedCaloriesFromActivityViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var burnedCaloriesFromActivityResponse = MutableLiveData<BurnedCaloriesFromActivityResponseModel>()
    private val repository = BurnedCaloriesFromActivityRepository()

    init {
        this.burnedCaloriesFromActivityResponse = repository.burnedCaloriesFromActivityResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callBurnedCaloriesFromActivityApi(activity: String?) {
        repository.burnedCaloriesFromActivityResponse(activity)
    }
}