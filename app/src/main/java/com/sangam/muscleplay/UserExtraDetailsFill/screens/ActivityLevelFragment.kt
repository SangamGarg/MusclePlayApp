package com.sangam.muscleplay.UserExtraDetailsFill.screens

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sangam.muscleplay.AppUtils.IntentUtil
import com.sangam.muscleplay.MainActivity
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserDataUtils.model.UserDataExtra
import com.sangam.muscleplay.UserExtraDetailsFill.UserDetailsActivity
import com.sangam.muscleplay.databinding.ErrorBottomDialogLayoutBinding
import com.sangam.muscleplay.databinding.FragmentActivityLevelBinding
import com.sangam.muscleplay.userRegistration.model.Measurements
import com.sangam.muscleplay.userRegistration.model.UserDetailsPostRequestBody
import com.sangam.muscleplay.userRegistration.model.UserDetailsResponseModel
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailData
import com.sangam.muscleplay.userRegistration.viewModel.UserDetailsViewModel

class ActivityLevelFragment : Fragment() {
    private val binding by lazy {
        FragmentActivityLevelBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }
    private lateinit var userDetailsViewModel: UserDetailsViewModel


    lateinit var activityLevel: String
    lateinit var goal: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        userDetailsViewModel = ViewModelProvider(this)[UserDetailsViewModel::class.java]
        observersForApiCalls()
        val view = inflater.inflate(R.layout.fragment_activity_level, container, false)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_array_activity_level, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerActivityLevel.adapter = adapter
        }
        binding.spinnerActivityLevel.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    val selectedItem = parent?.getItemAtPosition(position).toString()
                    when (selectedItem) {
                        "Sedentary: little or no exercise" -> activityLevel = "level_1"
                        "Exercise 1–3 times/week" -> activityLevel = "level_2"
                        "Exercise 4–5 times/week" -> activityLevel = "level_3"
                        "Daily exercise or intense exercise 3–4 times/week" -> activityLevel =
                            "level_4"

                        "Intense exercise 6–7 times/week" -> activityLevel = "level_5"
                        "Very intense exercise daily, or physical job" -> activityLevel = "level_6"
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

                }
            }


        val spinner2 = binding.spinnerGoals
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_array_goal, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            spinner2.adapter = adapter
        }
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "Extreme Weight Gain (1 kg)" -> goal = "Extreme WG"
                    "Normal Weight Gain (0.50 kg)" -> goal = "Normal WG"
                    "Mild Weight Gain (0.25 kg)" -> goal = "Mild WG"
                    "Extreme Weight Loss (1 kg)" -> goal = "Extreme WL"
                    "Normal Weight Loss (0.50 kg)" -> goal = "Normal WL"
                    "Mild Weight Loss (0.25 kg)" -> goal = "Mild WL"
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
        val photoUrl = requireArguments().getString("photoUrl").toString()
        val weight = requireArguments().getString("weight").toString()
        val hip = requireArguments().getString("hip").toString()
        val waist = requireArguments().getString("waist").toString()
        val neck = requireArguments().getString("neck").toString()

        binding.tvActivityFinish.setOnClickListener {
            callPutUserDetailsApi(
                auth.currentUser!!.uid, UserDetailsResponseModel(
                    auth.currentUser!!.uid,
                    UserDetailData.userDetailsResponseModel?.name,
                    UserDetailData.userDetailsResponseModel?.email,
                    UserDetailData.userDetailsResponseModel?.phone,
                    photoUrl,
                    gender,
                    age.toInt(),
                    Measurements(
                        height.toInt(),
                        weight.toInt(),
                        activityLevel,
                        goal,
                        hip.toInt(),
                        neck.toInt(),
                        waist.toInt()
                    )


                )
            )
        }
        return binding.root
    }

    private fun callPutUserDetailsApi(
        userId: String?, userDetailsResponseModel: UserDetailsResponseModel?
    ) {
        userDetailsViewModel.callPutUserDetails(userId, userDetailsResponseModel)
    }


    private fun observersForApiCalls() {

        userDetailsViewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showErrorBottomDialog(
                it
            )
        })
        userDetailsViewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })

        userDetailsViewModel.userDetailsResponse.observe(viewLifecycleOwner, Observer {
            userDetailsViewModel.setUserDetailsResponse(it)

            if (it.gender.isNullOrEmpty()) {
                IntentUtil.startIntent(requireContext(), UserDetailsActivity())
                activity?.finish()
            } else {
                IntentUtil.startIntent(requireContext(), MainActivity())
                activity?.finish()
            }
        })
    }

    private fun showErrorBottomDialog(message: String) {
        val bottomDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val dialogBinding = ErrorBottomDialogLayoutBinding.inflate(layoutInflater)
        bottomDialog.setContentView(dialogBinding.root)
        bottomDialog.setCancelable(false)
        bottomDialog.show()
        dialogBinding.messageTv.text = message
        dialogBinding.continueButton.setOnClickListener {
            bottomDialog.dismiss()
            activity?.finishAffinity()
        }
    }
}