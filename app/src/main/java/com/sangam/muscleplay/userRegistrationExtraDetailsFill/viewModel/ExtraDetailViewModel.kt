package com.sangam.muscleplay.userRegistrationExtraDetailsFill.viewModel

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


    fun changeToInch() {
        measureHeight.value = "in"
    }

    fun changeToInchHip() {
        measureHip.value = "in"
    }

    fun changeToInchWaist() {
        measureWaist.value = "in"
    }

    fun changeToInchNeck() {
        measureNeck.value = "in"
    }


    fun changeToMeters() {
        measureHeight.value = "m"
    }

    fun changeToMetersHip() {
        measureHip.value = "m"
    }

    fun changeToMetersWaist() {
        measureWaist.value = "m"
    }

    fun changeToMetersNeck() {
        measureNeck.value = "m"
    }

    fun changeToFeet() {
        measureHeight.value = "ft"

    }

    fun changeToFeetWaist() {
        measureWaist.value = "ft"

    }

    fun changeToFeetNeck() {
        measureNeck.value = "ft"

    }

    fun changeToFeetHip() {
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