package com.sangam.muscleplay.onBoardingScreen.screens

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.SignInAndSignUpActivities.SignInActivity


class SplashScreenFragment : Fragment() {
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        Handler(Looper.getMainLooper()).postDelayed(
            {
                if (onBoardingCheck()) {
                    if (firebaseAuth.currentUser != null) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_mainActivity)

                    } else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_signInActivity)

                    }
                } else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_viewPagerFragment)
                }
            }, 1000
        )
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    private fun onBoardingCheck(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }
}