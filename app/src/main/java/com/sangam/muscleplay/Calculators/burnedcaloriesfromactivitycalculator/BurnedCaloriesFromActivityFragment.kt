package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sangam.muscleplay.R

class BurnedCaloriesFromActivityFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_burned_calories_from_activity, container, false)
    }

}