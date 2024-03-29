package com.sangam.muscleplay.Calculators.fullStatsCalculator.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.Calculators.fullStatsCalculator.fullstatsrepository.FullStatsRepository
import com.sangam.muscleplay.botton_nav.home.model.BmiResponseModel
import com.sangam.muscleplay.botton_nav.home.model.BodyFatPercentageResponseModel
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieRequirementsResponseModel
import com.sangam.muscleplay.botton_nav.home.model.IdealWeightResponseModel
import com.sangam.muscleplay.botton_nav.home.repository.HomeRepository

class FullStatsViewModel : ViewModel() {
    var showProgressBmi = MutableLiveData<Boolean>()
    var showProgressIdealWeight = MutableLiveData<Boolean>()
    var showProgressDalyCalories = MutableLiveData<Boolean>()
    var showProgressBodyFat = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var bmiResponse = MutableLiveData<BmiResponseModel>()
    var idealWeightResponse = MutableLiveData<IdealWeightResponseModel>()
    var dailyCaloriesResponse = MutableLiveData<DailyCalorieRequirementsResponseModel>()
    var bodyFatResponse = MutableLiveData<BodyFatPercentageResponseModel>()
    private val repository = FullStatsRepository()

    init {
        this.bmiResponse = repository.bmiResponse
        this.idealWeightResponse = repository.idealWeightResponse
        this.dailyCaloriesResponse = repository.dailyCaloriesResponse
        this.bodyFatResponse = repository.bodyFatResponse
        this.showProgressBmi = repository.showProgressBmi
        this.showProgressIdealWeight = repository.showProgressIdealWeight
        this.showProgressDalyCalories = repository.showProgressDalyCalories
        this.showProgressBodyFat = repository.showProgressBodyFat
        this.errorMessage = repository.errorMessage
    }

    suspend fun callBmiApi(age: String?, weight: String?, height: String?) {
        repository.bmiApiResponse(age, weight, height)
    }

    suspend fun callIdealWeightApi(gender: String?, height: String?) {
        repository.idealWeightApiResponse(gender, height)
    }

    suspend fun callDailyCaloriesApi(
        age: String?, gender: String?, height: String?, weight: String?, activity_level: String?
    ) {
        repository.dailyCaloriesApiResponse(age, gender, height, weight, activity_level)
    }

    suspend fun callBodyFatApi(
        age: String?,
        gender: String?,
        weight: String?,
        height: String?,
        neck: String?,
        waist: String?,
        hip: String?
    ) {
        repository.bodyFatApiResponse(age, gender, weight, height, neck, waist, hip)
    }
}