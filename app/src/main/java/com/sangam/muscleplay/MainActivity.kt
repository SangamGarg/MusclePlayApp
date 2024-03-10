package com.sangam.muscleplay

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.UserExtraDetailsScreen.UserDetailsActivity
import com.sangam.muscleplay.UserDataUtils.UserViewModel
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity
import com.sangam.muscleplay.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val database by lazy {
        Firebase.firestore
    }
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityMainBinding
    private var dataFilled: Boolean? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this@MainActivity)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        if (firebaseAuth.currentUser == null) {
            IntentUtil.startIntent(this@MainActivity, SignInActivity())
        }

//        database.collection("users").document(firebaseAuth.currentUser!!.uid).get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val document = task.result
//                    if (document.exists()) {
//                        val name = document.getString("name")
//                        val email = document.getString("email")
//                        userViewModel.setUserData(name, email)
//                    }
//                } else {
//                    Toast.makeText(
//                        this,
//                        "Error getting user data: ${task.exception?.message}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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
            if (dataFilled == null) {
                IntentUtil.startIntent(this, UserDetailsActivity())
            } else {
                binding.loading.visibility = View.GONE

            }

        })
    }

    fun observeprogress() {
        userViewModel.showProgressExtra.observe(this, Observer {
            if (it) {
                binding.loading.visibility = View.VISIBLE
            }
        })
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        val currentDestination = navHostFragment?.findNavController()?.currentDestination?.id
        if (currentDestination == R.id.navigation_home) {
            finishAffinity()
            return
        }
        super.onBackPressed()
    }

}