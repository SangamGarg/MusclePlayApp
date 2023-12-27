package com.sangam.muscleplay.onBoardingScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity

class OnBoardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_board)
        HideStatusBarUtil.hideStatusBar(this@OnBoardActivity)

    }
}