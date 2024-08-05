package com.sangam.muscleplay.appUtils

import android.app.Activity
import android.content.Context
import android.content.Intent

object IntentUtil {
     lateinit var intent: Intent
     fun startIntent(context: Context, activity: Activity) {
        intent = Intent(context, activity::class.java)
        context.startActivity(intent)
    }
}
