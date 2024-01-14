package com.sangam.muscleplay.UserExtraDetailsScreen

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
        ViewModelProvider(this).get(UserViewModel::class.java)
    }
    private var dataFilled: Boolean? = null
    private val binding by lazy {
        ActivityUserDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@UserDetailsActivity)
        observeprogress()
        callGetUserExtraData()
        observeUserExtraData()
    }

    fun callGetUserExtraData() {
        userViewModel.getUserDataExtra()
    }

    fun observeUserExtraData() {
        userViewModel.userDataExtraResponse.observe(this, Observer {
            dataFilled = it.datafilled
            if (dataFilled != null) {
                IntentUtil.startIntent(this@UserDetailsActivity, MainActivity())
            } else {
                binding.loading.visibility = View.GONE
                binding.fragmentContainerView2.visibility = View.VISIBLE
            }

        })
    }

    fun observeprogress() {
        userViewModel.showProgressExtra.observe(this, Observer {
            if (it) {
                binding.loading.visibility = View.VISIBLE
                binding.fragmentContainerView2.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}