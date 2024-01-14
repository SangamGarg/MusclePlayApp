package com.sangam.muscleplay.Calculators.bmicalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentBmiBinding


class BmiCalculatorFragment : Fragment() {

    private val binding by lazy {
        FragmentBmiBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)


        return binding.root
    }

}