package com.sangam.muscleplay.calculators.dailycaloriescalculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DailyCaloriesViewModel : ViewModel() {
    var age = 24
    val measureHeight = MutableLiveData<String>("cm")
    val measureWeight = MutableLiveData<String>("Kg")
    fun increment() {
        age++
    }

    fun decrement() {
        age--
    }

    fun changeTometers() {
        measureHeight.value = "m"
    }

    fun changeToFeets() {
        measureHeight.value = "ft"

    }

    fun changeToCentimeters() {
        measureHeight.value = "cm"

    }

    fun changeToKilograms() {
        measureWeight.value = "kg"
    }

    fun changeToPounds() {
        measureWeight.value = "lbs"

    }

}