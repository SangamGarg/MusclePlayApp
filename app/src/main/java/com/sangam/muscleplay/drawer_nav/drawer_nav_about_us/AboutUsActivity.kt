package com.sangam.muscleplay.drawer_nav.drawer_nav_about_us

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityAboutUsBinding.inflate(layoutInflater)
    }
    private val database by lazy {
        FirebaseDatabase.getInstance()
    }
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        getData()
        initListener()
    }

    private fun initListener() {
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

    private fun getData() {
        binding.progreessBar.visibility = View.VISIBLE

//        val progressDialog = ProgressDialog(this)
//        progressDialog.setMessage("Loading...")
//        progressDialog.setCancelable(false)
//        progressDialog.show()

        databaseReference = database.getReference("AboutUs").child("text")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ABOUT_US", snapshot.toString())
                binding.progreessBar.visibility = View.GONE

                if (snapshot.exists()) {
                    val data = snapshot.getValue(String::class.java)
                    binding.tvAboutUs.text = data
                    binding.progreessBar.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                binding.progreessBar.visibility = View.GONE
                ToastUtil.makeToast(this@AboutUsActivity, error.message)
            }

        })
    }
}