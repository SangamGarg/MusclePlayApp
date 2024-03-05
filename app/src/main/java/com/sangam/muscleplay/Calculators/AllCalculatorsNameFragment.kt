package com.sangam.muscleplay.Calculators

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentAllCalculatorsNameBinding


class AllCalculatorsNameFragment : Fragment() {
    private val binding by lazy {
        FragmentAllCalculatorsNameBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all_calculators_name, container, false)
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.includeBmi.apply {
            tvNameOfCalculator.text = "Bmi Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.bmicalculator)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_bmiCalculatorFragment)

            }
        }
        binding.includeDailyCalories.apply {
            tvNameOfCalculator.text = "Daily Calories Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.dailycalories)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_dailyCaloriesCalculatorFragment)

            }
        }
        binding.includeIdealWeight.apply {
            tvNameOfCalculator.text = "Ideal Weight Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.idealbodyweight)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_idealWeightCalculatorFragment)
            }
        }
        binding.includeBurnedCaloriesFromActivity.apply {
            tvNameOfCalculator.text = "Burned Calories From An Activity Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.burnedcaloriesfromactivity)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_burnedCaloriesFromActivityFragment)

            }
        }
        binding.includeBodyMass.apply {
            tvNameOfCalculator.text = "Body Fat Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.bodyfat)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_bodyMassCalculatorFragment)

            }
        }
        binding.includeCaloriesInFood.apply {
            tvNameOfCalculator.text = "Calories In Food Calculator"
            ivImageOfCalculator.setImageResource(R.drawable.caloriesinfood)
            ivImageOfCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_allCalculatorsNameFragment_to_caloriesInFoodCalculatorFragment)

            }
        }

    }

}