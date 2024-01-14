package com.sangam.muscleplay.Calculators

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityAllCalculatorsBinding

class AllCalculatorsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAllCalculatorsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@AllCalculatorsActivity)

    }
}