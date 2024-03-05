package com.sangam.muscleplay.onBoardingScreen.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.AppUtils.NetworkConnection
import com.sangam.muscleplay.AppUtils.isInternetConnected
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity
import com.sangam.muscleplay.databinding.FragmentSplashScreenBinding


class SplashScreenFragment : Fragment() {
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val binding by lazy {
        FragmentSplashScreenBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                observeConnectionInternet()
            }, 1000
        )
        return binding.root
    }

    private fun observeConnectionInternet() {
        NetworkConnection(requireContext()).observe(viewLifecycleOwner, Observer {

            if (it) {


                if (onBoardCheck()) {
                    if (firebaseAuth.currentUser != null) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_mainActivity)

                    } else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_signInActivity)

                    }
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
                }
            } else {
                binding.textViewInternet.visibility = View.VISIBLE
                binding.imageViewInternet.visibility = View.VISIBLE
                binding.textViewOpenSetting.visibility = View.VISIBLE
                binding.splashImage.visibility = View.GONE
                binding.textViewOpenSetting.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
            }

        })
    }

    private fun onBoardCheck(): Boolean {
        val sharedPref =
            requireContext().getSharedPreferences("OnBoardScreenShow", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Done", false)
    }
}