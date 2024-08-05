package com.sangam.muscleplay.drawer_nav.drawer_nav_feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.appUtils.HideStatusBarUtil
import com.sangam.muscleplay.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFeedbackBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
            binding.root
        )
        HideStatusBarUtil.hideStatusBar(this@FeedbackActivity)
        binding.btnSubmitFeedback.setOnClickListener {
            giveFeedback()
        }
    }

    private fun giveFeedback() {
        binding.progressbarFeedback.visibility = View.VISIBLE
        val userFeed = binding.EtFeeback.text.toString()
        if (userFeed.isEmpty()) {
            binding.progressbarFeedback.visibility = View.GONE
            binding.EtFeeback.error = "Empty Field"
        } else {

        }
    }
}