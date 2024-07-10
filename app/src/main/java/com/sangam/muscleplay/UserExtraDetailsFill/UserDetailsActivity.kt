package com.sangam.muscleplay.UserExtraDetailsFill

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.databinding.ActivityUserDetailsBinding

class UserDetailsActivity : AppCompatActivity() {
    private val userViewModel by lazy {
        ViewModelProvider(this)[UserViewModel::class.java]
    }
    private var dataFilled: Boolean? = null
    private lateinit var binding: ActivityUserDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserDetailsBinding.inflate(layoutInflater)
        window.statusBarColor = Color.WHITE
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@UserDetailsActivity)
//        observeprogress()
//        callGetUserExtraData()
//        observeUserExtraData()
    }

//    private fun callGetUserExtraData() {
//        userViewModel.getUserDataExtra()
//    }
//
//    private fun observeUserExtraData() {
//        userViewModel.userDataExtraResponse.observe(this, Observer {
//            dataFilled = it.datafilled
//            if (dataFilled != null) {
//                IntentUtil.startIntent(this@UserDetailsActivity, MainActivity())
//            } else {
//                binding.loading.visibility = View.GONE
//                binding.fragmentContainerView2.visibility = View.VISIBLE
//            }
//
//        })
//    }
//
//    private fun observeprogress() {
//        userViewModel.showProgressExtra.observe(this, Observer {
//            if (it) {
//                binding.loading.visibility = View.VISIBLE
//                binding.fragmentContainerView2.visibility = View.GONE
//            }
//        })
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}