package com.sangam.muscleplay.Calculators.fullStatsCalculator

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sangam.muscleplay.AppUtils.AppArrays
import com.sangam.muscleplay.AppUtils.AppConvertUnitsUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.bodymasscalculator.BodyMassViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding
import com.sangam.muscleplay.databinding.FragmentFullStatsBinding

class FullStatsFragment : Fragment() {
    private val binding by lazy {
        FragmentFullStatsBinding.inflate(layoutInflater)
    }
    lateinit var homeViewModel: HomeViewModel
    private lateinit var viewModel: BodyMassViewModel
    private var numberPickerArrayWeight = emptyArray<String>()
    private var numberPickerArrayHeight = emptyArray<String>()
    lateinit var selectedItemHeight: String
    lateinit var gender: String
    lateinit var selectedItemWeight: String
    private var flag = 3
    private var numberPickerArrayNeck = emptyArray<String>()
    private var numberPickerArrayHip = emptyArray<String>()
    private var numberPickerArrayWaist = emptyArray<String>()
    lateinit var selectedItemNeck: String
    lateinit var selectedItemHip: String
    lateinit var selectedItemWaist: String
    lateinit var activityLevel: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        viewModel = ViewModelProvider(requireActivity())[BodyMassViewModel::class.java]
        val view = inflater.inflate(R.layout.fragment_full_stats, container, false)
        initListener()

        spinnerWaist()
        spinnerNeck()
        spinnerHip()
        observeHipUnit()
        observeWaistUnit()
        observeNeckUnit()
        observeHeightUnit()
        observeWeightUnit()
        setText()

        spinnerHeight()
        spinnerWeight()

        return binding.root
    }

    private fun initListener() {
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


        binding.calculateStatsButton.setOnClickListener {
            val height = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemHeight, numberPickerArrayHeight, binding.numberPickerHeight
            )
            val weight = AppConvertUnitsUtil.convertUnitWeight(
                selectedItemWeight, numberPickerArrayWeight, binding.numberPickerWeight
            )
            val age = binding.tvAgeLive.text.toString()
            val waist = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemWaist, numberPickerArrayWaist, binding.numberPickerWaist
            )
            val hip = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemHip, numberPickerArrayHip, binding.numberPickerHip
            )
            val neck = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemNeck, numberPickerArrayNeck, binding.numberPickerNeck
            )

            if (flag == 3) {
                ToastUtil.makeToast(requireContext(), "Please select a gender")
            } else {

            }
        }




        binding.decrement.setOnClickListener {
            if (viewModel.age == 1) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
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

    }

    private fun observeWaistUnit() {
        viewModel.measureWaist.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsWaist.text = it
        })
    }

    private fun observeHipUnit() {
        viewModel.measureHip.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsHip.text = it
        })
    }

    private fun observeNeckUnit() {
        viewModel.measureNeck.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsNeck.text = it
        })
    }

    private fun spinnerWaist() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_length_measurements, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerWaist.adapter = adapter
        }
        binding.spinnerWaist.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemWaist = parent?.getItemAtPosition(position).toString()
                when (selectedItemWaist) {
                    "centimeters (cm)" -> {
                        viewModel.changeToCentimetersWaist()

                        binding.numberPickerWaist.minValue = 0
                        binding.numberPickerWaist.maxValue = AppArrays.numbersArrayCm40130.size - 1
                        binding.numberPickerWaist.displayedValues = AppArrays.numbersArrayCm40130
                        numberPickerArrayWaist = AppArrays.numbersArrayCm40130
                    }

                    "meters (m)" -> {
                        viewModel.changeTometersWaist()
                        binding.numberPickerWaist.minValue = 0
                        binding.numberPickerWaist.maxValue =
                            AppArrays.numbersArrayMeter40130.size - 1
                        binding.numberPickerWaist.displayedValues = AppArrays.numbersArrayMeter40130
                        numberPickerArrayWaist = AppArrays.numbersArrayMeter40130

                    }

                    "feet (ft)" -> {
                        viewModel.changeToFeetsWaist()
                        binding.numberPickerWaist.minValue = 0
                        binding.numberPickerWaist.maxValue =
                            AppArrays.numbersArrayFeet40130.size - 1
                        binding.numberPickerWaist.displayedValues = AppArrays.numbersArrayFeet40130
                        numberPickerArrayWaist = AppArrays.numbersArrayFeet40130

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun spinnerNeck() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_length_measurements, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerNeck.adapter = adapter
        }
        binding.spinnerNeck.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemNeck = parent?.getItemAtPosition(position).toString()
                when (selectedItemNeck) {
                    "centimeters (cm)" -> {
                        viewModel.changeToCentimetersNeck()

                        binding.numberPickerNeck.minValue = 0
                        binding.numberPickerNeck.maxValue = AppArrays.numbersArrayCm2080.size - 1
                        binding.numberPickerNeck.displayedValues = AppArrays.numbersArrayCm2080
                        numberPickerArrayNeck = AppArrays.numbersArrayCm2080
                    }

                    "meters (m)" -> {
                        viewModel.changeTometersNeck()
                        binding.numberPickerNeck.minValue = 0
                        binding.numberPickerNeck.maxValue = AppArrays.numbersArrayMeter2080.size - 1
                        binding.numberPickerNeck.displayedValues = AppArrays.numbersArrayMeter2080
                        numberPickerArrayNeck = AppArrays.numbersArrayMeter2080

                    }

                    "feet (ft)" -> {
                        viewModel.changeToFeetsNeck()
                        binding.numberPickerNeck.minValue = 0
                        binding.numberPickerNeck.maxValue = AppArrays.numbersArrayFeet2080.size - 1
                        binding.numberPickerNeck.displayedValues = AppArrays.numbersArrayFeet2080
                        numberPickerArrayNeck = AppArrays.numbersArrayFeet2080

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun spinnerHip() {
        ArrayAdapter.createFromResource(
            requireContext(), R.array.spinner_length_measurements, R.layout.custom_spinner_layout
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerHip.adapter = adapter
        }
        binding.spinnerHip.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItemHip = parent?.getItemAtPosition(position).toString()
                when (selectedItemHip) {
                    "centimeters (cm)" -> {
                        viewModel.changeToCentimetersHip()

                        binding.numberPickerHip.minValue = 0
                        binding.numberPickerHip.maxValue = AppArrays.numbersArrayCm40130.size - 1
                        binding.numberPickerHip.displayedValues = AppArrays.numbersArrayCm40130
                        numberPickerArrayHip = AppArrays.numbersArrayCm40130
                    }

                    "meters (m)" -> {
                        viewModel.changeTometersHip()
                        binding.numberPickerHip.minValue = 0
                        binding.numberPickerHip.maxValue = AppArrays.numbersArrayMeter40130.size - 1
                        binding.numberPickerHip.displayedValues = AppArrays.numbersArrayMeter40130
                        numberPickerArrayHip = AppArrays.numbersArrayMeter40130

                    }

                    "feet (ft)" -> {
                        viewModel.changeToFeetsHip()
                        binding.numberPickerHip.minValue = 0
                        binding.numberPickerHip.maxValue = AppArrays.numbersArrayFeet40130.size - 1
                        binding.numberPickerHip.displayedValues = AppArrays.numbersArrayFeet40130
                        numberPickerArrayHip = AppArrays.numbersArrayFeet40130

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
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
            requireContext(), R.array.spinner_length_measurements, R.layout.custom_spinner_layout
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
                        viewModel.changeTometers()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArrayHeight = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        viewModel.changeToFeets()
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