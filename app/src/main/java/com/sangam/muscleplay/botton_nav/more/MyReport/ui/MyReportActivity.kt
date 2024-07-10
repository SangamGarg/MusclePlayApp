package com.sangam.muscleplay.botton_nav.more.MyReport.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.databinding.ActivityMyReportBinding

class MyReportActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMyReportBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
    }
}