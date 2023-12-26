package com.sangam.muscleplay.AppUtils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

object HideStatusBarUtil {
    fun hideStatusBar(context: Context) {
        (context as? AppCompatActivity)?.supportActionBar?.hide()
        (context as? AppCompatActivity)?.window?.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            (context as? AppCompatActivity)?.getWindow()
                ?.getAttributes()?.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }
}