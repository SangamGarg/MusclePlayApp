package com.sangam.muscleplay.UserExtraDetailsFill.screens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentGenderBinding

class GenderFragment : Fragment() {
    private var flag = 3
    private val binding by lazy {
        FragmentGenderBinding.inflate(layoutInflater)
    }
    lateinit var gender: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gender, container, false)

        chooseGenderFunction()
        val photoUrl = requireArguments().getString("photoUrl").toString()

        binding.tvGenderNext.setOnClickListener {
//            val selectedRadioButton = view.findViewById<View>(radioGroup.checkedRadioButtonId)
            if (flag == 3) {
                Toast.makeText(requireContext(), "Please select a gender first", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val bundle = Bundle()
                bundle.putString(
                    "gender", gender
                )
                bundle.putString(
                    "photoUrl", photoUrl
                )
                findNavController().navigate(
                    R.id.action_genderFragment_to_ageHeightWeightFragment, bundle
                )
            }
        }
        return binding.root
    }

    private fun chooseGenderFunction() {
        binding.cardViewMale.apply {

            setOnClickListener {
                startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
                )
                if (flag == 1) {
                    binding.cardViewFemale.strokeColor = Color.WHITE
                    binding.tickFemale.visibility = View.GONE
                }
                binding.cardViewMale.strokeColor = Color.parseColor("#FFBB86FC")
                binding.tickMale.visibility = View.VISIBLE
                gender = "male"
                flag = 0
            }
        }
        binding.cardViewFemale.apply {
            setOnClickListener {
                startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
                )
                if (flag == 0) {
                    binding.cardViewMale.strokeColor = Color.WHITE
                    binding.tickMale.visibility = View.GONE
                }
                binding.cardViewFemale.strokeColor = Color.parseColor("#FFBB86FC")
                binding.tickFemale.visibility = View.VISIBLE
                gender = "female"
                flag = 1

            }
        }
    }

}
