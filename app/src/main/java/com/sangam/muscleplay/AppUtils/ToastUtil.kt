package com.sangam.muscleplay.AppUtils

import android.content.Context
import android.widget.Toast

object ToastUtil {
    fun makeToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}