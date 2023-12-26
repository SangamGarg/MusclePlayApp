package com.sangam.muscleplay.onBoardingScreen.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.sangam.muscleplay.R

class OnBoardFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_on_board2, container, false)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        view.findViewById<TextView>(R.id.btnNext).setOnClickListener {
            viewPager?.currentItem = 2
            val bounce: Animation = AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
            it.startAnimation(bounce)
        }
        return view
    }

}