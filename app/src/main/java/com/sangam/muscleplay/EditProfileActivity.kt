package com.sangam.muscleplay

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        HideStatusBarUtil.hideStatusBar(this@EditProfileActivity)

    }
}