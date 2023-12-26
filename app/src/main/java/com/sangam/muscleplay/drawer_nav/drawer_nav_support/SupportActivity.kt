package com.sangam.muscleplay.drawer_nav.drawer_nav_support

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.R

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)
        HideStatusBarUtil.hideStatusBar(this)

    }
}