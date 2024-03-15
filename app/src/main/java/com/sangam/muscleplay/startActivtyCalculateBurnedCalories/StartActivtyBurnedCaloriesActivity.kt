package com.sangam.muscleplay.startActivtyCalculateBurnedCalories

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityStartActivtyBurnedCaloriesBinding

class StartActivtyBurnedCaloriesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityStartActivtyBurnedCaloriesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}