package com.sangam.muscleplay.drawer_nav.drawer_nav_feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityFeedbackBinding

class FeedbackActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFeedbackBinding.inflate(layoutInflater)
    }
    private val database by lazy {
        FirebaseDatabase.getInstance()
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
        val userfeed = binding.EtFeeback.text.toString()
        if (userfeed.isEmpty()) {
            binding.progressbarFeedback.visibility = View.GONE
            binding.EtFeeback.error = "Empty Field"
        } else {
            database.reference.child("Feedbacks")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(userfeed)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        binding.progressbarFeedback.visibility = View.GONE

                        Toast.makeText(this, "Feedback Submitted Successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        binding.progressbarFeedback.visibility = View.GONE

                        Toast.makeText(
                            this, "Error : ${it.exception?.message.toString()}", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}