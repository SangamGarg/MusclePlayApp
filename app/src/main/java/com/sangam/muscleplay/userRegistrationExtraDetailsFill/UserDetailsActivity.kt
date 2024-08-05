package com.sangam.muscleplay.userRegistrationExtraDetailsFill

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        window.statusBarColor = Color.WHITE
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@UserDetailsActivity)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}