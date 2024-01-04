package com.sangam.muscleplay.ExtraDetailsScreen.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.sangam.muscleplay.R

class WaistHipNeckFragment : Fragment() {

    private var hipValue: Int = 40
    private var neckValue: Int = 20
    private var waistValue: Int = 40
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_waist_hip_neck, container, false)
        val gender = requireArguments().getString("gender").toString()
        val age = requireArguments().getString("age").toString()
        val height = requireArguments().getString("height").toString()
        val weight = requireArguments().getString("weight").toString()
        setupSliderListeners(view)

        view.findViewById<TextView>(R.id.tvWaistNext).setOnClickListener {
            findNavController().navigate(R.id.action_waistHipNeckFragment_to_activityLevelFragment,
                Bundle().apply {
                    putString("waist", waistValue.toString())
                    putString("hip", hipValue.toString())
                    putString("neck", neckValue.toString())
                    putString("age", age)
                    putString("height", height)
                    putString("weight", weight)
                    putString("gender", gender)
                })
        }
        return view
    }


    private fun setupSliderListeners(view: View) {
        val hipSlider = view.findViewById<Slider>(R.id.sliderHip)
        val neckSlider = view.findViewById<Slider>(R.id.sliderNeck)
        val waistSlider = view.findViewById<Slider>(R.id.sliderWaist)

        hipSlider.addOnChangeListener { slider, value, fromUser ->
            hipValue = value.toInt()
            view.findViewById<TextView>(R.id.tvHipLive).text = hipValue.toString()

        }

        neckSlider.addOnChangeListener { slider, value, fromUser ->
            neckValue = value.toInt()
            view.findViewById<TextView>(R.id.tvNeckLive).text = neckValue.toString()

        }

        waistSlider.addOnChangeListener { slider, value, fromUser ->
            waistValue = value.toInt()
            view.findViewById<TextView>(R.id.tvWaistLive).text = waistValue.toString()

        }
    }

}