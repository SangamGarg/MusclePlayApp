package com.sangam.muscleplay.drawer_nav.drawer_nav_support

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.sangam.muscleplay.databinding.ActivitySupportBinding

class SupportActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySupportBinding.inflate(layoutInflater)
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

    }

    private fun getData() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        databaseReference = database.getReference("Support").child("text")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("SUPPORT", snapshot.toString())
                progressDialog.dismiss()

                if (snapshot.exists()) {
                    val data = snapshot.getValue(String::class.java)
                    binding.tvSupportText.text = data
                    progressDialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressDialog.dismiss()
                ToastUtil.makeToast(this@SupportActivity, error.message)
            }

        })
    }
}