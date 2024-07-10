package com.sangam.muscleplay.UserExtraDetailsFill.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExtraDetailViewModel : ViewModel() {
    var age = 24
    val measureHeight = MutableLiveData<String>("cm")
    val measureHip = MutableLiveData<String>("cm")
    val measureNeck = MutableLiveData<String>("cm")
    val measureWaist = MutableLiveData<String>("cm")
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

    fun changeTometersHip() {
        measureHip.value = "m"
    }

    fun changeTometersWaist() {
        measureWaist.value = "m"
    }

    fun changeTometersNeck() {
        measureNeck.value = "m"
    }

    fun changeToFeets() {
        measureHeight.value = "ft"

    }

    fun changeToFeetsWaist() {
        measureWaist.value = "ft"

    }

    fun changeToFeetsNeck() {
        measureNeck.value = "ft"

    }

    fun changeToFeetsHip() {
        measureHip.value = "ft"
    }

    fun changeToCentimeters() {
        measureHeight.value = "cm"
    }
    fun changeToCentimetersNeck() {
        measureNeck.value = "cm"
    }
    fun changeToCentimetersWaist() {
        measureWaist.value = "cm"

    }
    fun changeToCentimetersHip() {
        measureHip.value = "cm"
    }

    fun changeToKilograms() {
        measureWeight.value = "kg"
    }

    fun changeToPounds() {
        measureWeight.value = "lbs"

    }

}