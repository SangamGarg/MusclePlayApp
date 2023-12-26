package com.sangam.muscleplay.drawer_nav.drawer_nav_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.R

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        HideStatusBarUtil.hideStatusBar(this)

    }
}