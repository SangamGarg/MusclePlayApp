package com.sangam.muscleplay.ExtraDetailsScreen.screens

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserDataUtils.model.UserDataExtra

class ActivityLevelFragment : Fragment() {

    private val database by lazy {
        Firebase.firestore
    }
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    lateinit var activityLevel: String
    var datafilled: Boolean? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_level, container, false)
        val spinner = view.findViewById<Spinner>(R.id.spinnerActivityLevel)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_array_activity_level, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Sedentary: little or no exercise" -> activityLevel = "level_1"
                    "Exercise 1–3 times/week" -> activityLevel = "level_2"
                    "Exercise 4–5 times/week" -> activityLevel = "level_3"
                    "Daily exercise or intense exercise 3–4 times/week" -> activityLevel = "level_4"
                    "Intense exercise 6–7 times/week" -> activityLevel = "level_5"
                    "Very intense exercise daily, or physical job" -> activityLevel = "level_6"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }

        // Inflate the layout for this fragment
        val gender = requireArguments().getString("gender").toString()
        val age = requireArguments().getString("age").toString()
        val height = requireArguments().getString("height").toString()
        val weight = requireArguments().getString("weight").toString()
        val hip = requireArguments().getString("hip").toString()
        val waist = requireArguments().getString("waist").toString()
        val neck = requireArguments().getString("neck").toString()

        view.findViewById<TextView>(R.id.tvActivityFinish).setOnClickListener {
            datafilled = true
            val userId = auth.currentUser?.uid ?: ""
            val userDataExtra =
                UserDataExtra(
                    datafilled,
                    age,
                    gender,
                    height,
                    weight,
                    hip,
                    neck,
                    waist,
                    activityLevel
                )

            storeUserExtraDetails(userId, userDataExtra)
            findNavController().navigate(R.id.action_activityLevelFragment_to_mainActivity2)

        }
        return view
    }

    private fun storeUserExtraDetails(userId: String, userDataExtra: UserDataExtra) {
        database.collection("users").document(userId).update(userDataExtra.toMap())
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Details stored successfully!", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to store details!", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun UserDataExtra.toMap(): Map<String, Any?> {
        return mapOf(
            "datafilled" to datafilled,
            "age" to age,
            "gender" to gender,
            "height" to height,
            "weight" to weight,
            "hip" to hip,
            "neck" to neck,
            "waist" to waist,
            "activity_level" to activity_level
        )
    }
}