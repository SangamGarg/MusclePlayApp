package com.sangam.muscleplay.onBoardingScreen.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.sangam.muscleplay.R

class OnBoardFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board3, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        view.findViewById<TextView>(R.id.btnSignInOnBoard).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signInActivity)
        }
        view.findViewById<TextView>(R.id.btnSignUpOnBoard).setOnClickListener {
            findNavController().navigate(R.id.action_viewPagerFragment_to_signUpActivity)
        }
        return view
    }

}
