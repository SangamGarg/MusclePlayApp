package com.sangam.muscleplay.UserExtraDetailsScreen.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sangam.muscleplay.R

class GenderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gender, container, false)

        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val nextButton = view.findViewById<TextView>(R.id.tvGenderNext)

        // Disable nextButton initially
//        nextButton.isEnabled = false
//
        var gender: String? = null
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonMale -> gender = "male"
                R.id.radioButtonFemale -> gender = "female"
            }
        }

        nextButton.setOnClickListener {
//            val selectedRadioButton = view.findViewById<View>(radioGroup.checkedRadioButtonId)
            if (gender == null) {
                Toast.makeText(requireContext(), "Please select a gender first", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val bundle = Bundle()
                bundle.putString(
                    "gender", gender
                )
                findNavController().navigate(
                    R.id.action_genderFragment_to_ageHeightWeightFragment, bundle
                )
            }
        }
        return view
    }

}
