package com.sangam.muscleplay.drawer_nav.drawer_nav_refer_and_earn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.R

class ReferAndEarnActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refer_and_earn)
        HideStatusBarUtil.hideStatusBar(this)

    }
}