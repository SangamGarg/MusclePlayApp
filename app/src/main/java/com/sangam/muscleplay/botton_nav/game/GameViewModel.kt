package com.sangam.muscleplay.botton_nav.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Game Fragment"
    }
    val text: LiveData<String> = _text
}