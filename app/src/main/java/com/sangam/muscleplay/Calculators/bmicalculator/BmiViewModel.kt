package com.sangam.muscleplay.Calculators.bmicalculator

import androidx.lifecycle.ViewModel

class BmiViewModel : ViewModel() {
    var age = 40
    fun increment() {
        age++
    }

    fun decrement() {
        age--
    }
}