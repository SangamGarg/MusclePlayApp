package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.repository.BurnedCaloriesFromActivityRepository

class BurnedCaloriesFromActivityViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var burnedCaloriesFromActivityResponse = MutableLiveData<BurnedCaloriesFromActivityModel>()
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