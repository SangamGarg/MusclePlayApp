package com.sangam.muscleplay.UserExtraDetailsFill.screens

import android.os.Bundle
import android.util.Log
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
import com.sangam.muscleplay.AppUtils.AppArrays
import com.sangam.muscleplay.AppUtils.AppConvertUnitsUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.UserExtraDetailsFill.viewModel.ExtraDetailViewModel
import com.sangam.muscleplay.databinding.FragmentWaistHipNeckBinding

class WaistHipNeckFragment : Fragment() {
    private val binding by lazy {
        FragmentWaistHipNeckBinding.inflate(layoutInflater)
    }
    private var numberPickerArrayNeck = emptyArray<String>()
    private var numberPickerArrayHip = emptyArray<String>()
    private var numberPickerArrayWaist = emptyArray<String>()
    lateinit var viewModel: ExtraDetailViewModel
    lateinit var selectedItemNeck: String
    lateinit var selectedItemHip: String
    lateinit var selectedItemWaist: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[ExtraDetailViewModel::class.java]
        val view = inflater.inflate(R.layout.fragment_waist_hip_neck, container, false)
        initListener()

        return binding.root
    }

    private fun initListener() {
        val gender = requireArguments().getString("gender").toString()
        val age = requireArguments().getString("age").toString()
        val photoUrl = requireArguments().getString("photoUrl").toString()
        val height = requireArguments().getString("height").toString()
        val weight = requireArguments().getString("weight").toString()

        binding.tvWaistNext.setOnClickListener {
            val waist = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemWaist, numberPickerArrayWaist, binding.numberPickerWaist
            )
            val hip = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemHip, numberPickerArrayHip, binding.numberPickerHip
            )
            val neck = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemNeck, numberPickerArrayNeck, binding.numberPickerNeck
            )

            Log.d("TESTFORNECKWASIST", "hip $hip neck $neck waist $waist")

            findNavController().navigate(
                R.id.action_waistHipNeckFragment_to_activityLevelFragment,
                Bundle().apply {
                    putString("waist", waist)
                    putString("hip", hip)
                    putString("neck", neck)
                    putString("age", age)
                    putString("height", height)
                    putString("weight", weight)
                    putString("gender", gender)
                    putString("photoUrl", photoUrl)
                })
        }
        spinnerWaist()
        spinnerNeck()
        spinnerHip()
        observeHipUnit()
        observeWaistUnit()
        observeNeckUnit()

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
}