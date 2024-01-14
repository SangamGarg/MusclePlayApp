package com.sangam.muscleplay.UserExtraDetailsScreen.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.sangam.muscleplay.R

class AgeHeightWeightFragment : Fragment() {
    private var ageValue: Int = 1
    private var heightValue: Int = 130
    private var weightValue: Int = 40
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_age_height_weight, container, false)
        val gender = requireArguments().getString("gender").toString()
        setupSliderListeners(view)

        view.findViewById<TextView>(R.id.tvAgeNext).setOnClickListener {
            findNavController().navigate(
                R.id.action_ageHeightWeightFragment_to_waistHipNeckFragment,
                Bundle().apply {
                    putString("age", ageValue.toString())
                    putString("height", heightValue.toString())
                    putString("weight", weightValue.toString())
                    putString("gender", gender)
                })
        }
        return view
    }


    private fun setupSliderListeners(view: View) {
        val ageSlider = view.findViewById<Slider>(R.id.sliderAge)
        val heightSlider = view.findViewById<Slider>(R.id.sliderHeight)
        val weightSlider = view.findViewById<Slider>(R.id.sliderWeight)

        ageSlider.addOnChangeListener { slider, value, fromUser ->
            ageValue = value.toInt()
            view.findViewById<TextView>(R.id.tvAgeLive).text = ageValue.toString()
        }

        heightSlider.addOnChangeListener { slider, value, fromUser ->
            heightValue = value.toInt()
            view.findViewById<TextView>(R.id.tvHeightLive).text = heightValue.toString()

        }

        weightSlider.addOnChangeListener { slider, value, fromUser ->
            weightValue = value.toInt()
            view.findViewById<TextView>(R.id.tvWeightLive).text = weightValue.toString()

        }
    }

}