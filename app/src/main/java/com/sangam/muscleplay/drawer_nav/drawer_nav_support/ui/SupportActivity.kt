package com.sangam.muscleplay.drawer_nav.drawer_nav_support.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySupportBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        initListener()

    }

    private fun initListener() {
        binding.tvSupportText.text = "formattedData"
    }
}