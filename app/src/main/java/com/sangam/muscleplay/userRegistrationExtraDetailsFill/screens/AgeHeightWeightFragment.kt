package com.sangam.muscleplay.userRegistrationExtraDetailsFill.screens

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sangam.muscleplay.appUtils.AppArrays
import com.sangam.muscleplay.appUtils.AppConvertUnitsUtil
import com.sangam.muscleplay.appUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.userRegistrationExtraDetailsFill.viewModel.ExtraDetailViewModel
import com.sangam.muscleplay.databinding.FragmentAgeHeightWeightBinding

class AgeHeightWeightFragment : Fragment() {
    private val binding by lazy {
        FragmentAgeHeightWeightBinding.inflate(layoutInflater)
    }
    private var numberPickerArrayWeight = emptyArray<String>()
    lateinit var selectedItemWeight: String

    private var numberPickerArrayHeight = emptyArray<String>()
    lateinit var viewModel: ExtraDetailViewModel
    lateinit var selectedItemHeight: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_age_height_weight, container, false)
        viewModel = ViewModelProvider(this)[ExtraDetailViewModel::class.java]
        activity?.window?.statusBarColor = Color.WHITE

//        setupSliderListeners(view)


        initListener()
        observeHeightUnit()
        observeWeightUnit()

        setText()
        return binding.root
    }

    private fun initListener() {

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }


        val gender = requireArguments().getString("gender").toString()
        val photoUrl = requireArguments().getString("photoUrl").toString()


        binding.tvAgeNext.setOnClickListener {
            val height = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemHeight, numberPickerArrayHeight, binding.numberPickerHeight
            )
            val weight = AppConvertUnitsUtil.convertUnitWeight(
                selectedItemWeight, numberPickerArrayWeight, binding.numberPickerWeight
            )
            val age = binding.tvAgeLive.text.toString()

            findNavController().navigate(R.id.action_ageHeightWeightFragment_to_waistHipNeckFragment,
                Bundle().apply {
                    putString("age", age)
                    putString("height", height)
                    putString("weight", weight)
                    putString("gender", gender)
                    putString("photoUrl", photoUrl)
                })
        }

        binding.decrement.setOnClickListener {
            if (viewModel.age == 1) {
                ToastUtil.makeToast(requireContext(), "Minimum Value Achieved")
            } else {
                viewModel.decrement()
                setText()
            }
        }
        binding.increment.setOnClickListener {
            if (viewModel.age == 80) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
            } else {
                viewModel.increment()
                setText()
            }
        }

        spinnerHeight()
        spinnerWeight()


    }


    private fun spinnerWeight() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_weight_measurements, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerWeight.adapter = adapter
        }
        binding.spinnerWeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemWeight = parent?.getItemAtPosition(position).toString()
                when (selectedItemWeight) {
                    "kilograms (kg)" -> {
                        viewModel.changeToKilograms()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayKg.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayKg
                        numberPickerArrayWeight = AppArrays.numbersArrayKg

                    }

                    "pound (lbs)" -> {
                        viewModel.changeToPounds()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayPounds.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayPounds
                        numberPickerArrayWeight = AppArrays.numbersArrayPounds

                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun spinnerHeight() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_length_measurements_height,
            R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerHeight.adapter = adapter
        }
        binding.spinnerHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemHeight = parent?.getItemAtPosition(position).toString()
                when (selectedItemHeight) {
                    "centimeters (cm)" -> {
                        viewModel.changeToCentimeters()

                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayCm.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayCm
                        numberPickerArrayHeight = AppArrays.numbersArrayCm
                    }

                    "meters (m)" -> {
                        viewModel.changeToMeters()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArrayHeight = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        viewModel.changeToFeet()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayFeet.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayFeet
                        numberPickerArrayHeight = AppArrays.numbersArrayFeet

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun observeWeightUnit() {
        viewModel.measureWeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsWeight.text = it
        })
    }

    private fun observeHeightUnit() {
        viewModel.measureHeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsHeight.text = it
        })
    }

    fun setText() {
        binding.tvAgeLive.text = viewModel.age.toString()
    }


}