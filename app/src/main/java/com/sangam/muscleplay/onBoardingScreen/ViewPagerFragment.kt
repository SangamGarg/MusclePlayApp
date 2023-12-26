package com.sangam.muscleplay.onBoardingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.sangam.muscleplay.R
import com.sangam.muscleplay.onBoardingScreen.adapter.ViewPagerAdapter
import com.sangam.muscleplay.onBoardingScreen.screens.OnBoardFragment1
import com.sangam.muscleplay.onBoardingScreen.screens.OnBoardFragment2
import com.sangam.muscleplay.onBoardingScreen.screens.OnBoardFragment3

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)
        val fragmentList = arrayListOf<Fragment>(
            OnBoardFragment1(),
            OnBoardFragment2(),
            OnBoardFragment3()
        )
        val adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)

        val pager = view.findViewById<ViewPager2>(R.id.viewPager)
        pager.adapter = adapter
        return view
    }
}