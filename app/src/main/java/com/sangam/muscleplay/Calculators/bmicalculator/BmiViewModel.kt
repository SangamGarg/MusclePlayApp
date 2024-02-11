package com.sangam.muscleplay.Calculators.bmicalculator

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BmiViewModel : ViewModel() {
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