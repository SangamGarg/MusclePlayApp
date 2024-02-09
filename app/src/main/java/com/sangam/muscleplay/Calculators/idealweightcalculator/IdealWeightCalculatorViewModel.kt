package com.sangam.muscleplay.Calculators.idealweightcalculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IdealWeightCalculatorViewModel : ViewModel() {
    val measure = MutableLiveData<String>("cm")
    fun changeTometers() {
        measure.value = "m"
    }

    fun changeToFeets() {
        measure.value = "ft"

    }
    fun changeToCentimeters() {
        measure.value = "cm"

    }
}