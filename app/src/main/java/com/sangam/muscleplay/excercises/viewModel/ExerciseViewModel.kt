package com.sangam.muscleplay.excercises.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityResponseModel
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.repository.BurnedCaloriesFromActivityRepository
import com.sangam.muscleplay.excercises.model.ExerciseResponseModel
import com.sangam.muscleplay.excercises.respository.ExerciseRepository

class ExerciseViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var exerciseResponse = MutableLiveData<ExerciseResponseModel>()
    private val repository = ExerciseRepository()

    init {
        this.exerciseResponse = repository.exerciseResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callExerciseApi(
        name: String?, muscle: String?, type: String?, difficulty: String?
    ) {
        repository.exerciseApiResponse(name, muscle, type, difficulty)
    }
}