package com.sangam.muscleplay.botton_nav.more.MyProgress

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.databinding.ActivityMyProgressBinding

class MyProgressActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMyProgressBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@MyProgressActivity)
    }
}