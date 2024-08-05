package com.sangam.muscleplay.drawer_nav.drawer_nav_about_us.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        initListener()
    }

    private fun initListener() {
        binding.tvAboutUs.text = "formattedData"


        binding.imageViewFacebook.setOnClickListener {

        }
        binding.imageViewInsta.setOnClickListener {
//            val appPackageName = "com.instagram.android"
//            val webUrl = "https://instagram.com/sangamgarg19?igshid=OTk0YzhjMDVlZA=="
//            openIntentWeb(appPackageName, webUrl)
        }
        binding.imageViewTwitter.setOnClickListener {

        }
        binding.imageViewYoutube.setOnClickListener {
//            val appPackageName = "com.google.android.youtube"
//            val webUrl = "https://www.youtube.com/@sangamgarg19/featured"
//            openIntentWeb(appPackageName, webUrl)
        }
    }

    fun openIntentWeb(appPackageName: String, webUrl: String) {
        try {
            val intent = packageManager.getLaunchIntentForPackage(appPackageName)
            if (intent != null) {
                intent.action = Intent.ACTION_VIEW
                intent.data = Uri.parse(webUrl)
                startActivity(intent)
            } else {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                startActivity(webIntent)
            }
        } catch (e: ActivityNotFoundException) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
            startActivity(webIntent)
        }
    }
}