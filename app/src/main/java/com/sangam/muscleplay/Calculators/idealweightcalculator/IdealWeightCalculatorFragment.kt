package com.sangam.muscleplay.Calculators.idealweightcalculator

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sangam.muscleplay.AppUtils.AppArrays
import com.sangam.muscleplay.AppUtils.AppConvertUnitsUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentIdealWeightCalculatorBinding

class IdealWeightCalculatorFragment : Fragment() {
    private val binding by lazy {
        FragmentIdealWeightCalculatorBinding.inflate(layoutInflater)
    }
    private var numberPickerArray = emptyArray<String>()
    lateinit var viewModel: HomeViewModel
    lateinit var height: String
    lateinit var gender: String
    private var flag = 3
    lateinit var selectedItem: String
    lateinit var idealWeightCalculatorViewModel: IdealWeightCalculatorViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ideal_weight_calculator, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        idealWeightCalculatorViewModel =
            ViewModelProvider(this).get(IdealWeightCalculatorViewModel::class.java)
        initListener()
        observeWeightUnit()
        observePorgress()
        observerErrorMessageApiResponse()
        observerIdealWeightApiResponse()
        return binding.root
    }

    private fun initListener() {

        binding.cardViewMale.apply {

            setOnClickListener {
                startAnimation(
                    AnimationUtils.loadAnimation(requireContext(), R.anim.bounce)
                )
                if (flag == 1) {
                    binding.linearlayoutFemale.setBackgroundColor(Color.WHITE)
                }
                binding.linearlayoutMale.setBackgroundColor(Color.GREEN)
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
                    binding.linearlayoutMale.setBackgroundColor(Color.WHITE)
                }
                binding.linearlayoutFemale.setBackgroundColor(Color.GREEN)
                gender = "female"
                flag = 1

            }
        }

        binding.calculateIdealWeightButton.setOnClickListener {
            val height = AppConvertUnitsUtil.convertUnitHeight(
                selectedItem, numberPickerArray, binding.numberPicker
            )
//            ToastUtil.makeToast(requireContext(), height)

            if (flag == 3) {
                ToastUtil.makeToast(requireContext(), "Please select a gender")
            } else {
                callIdealWeightApi(gender, height)
            }
        }
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_length_measurements,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
            binding.spinnerHeight.adapter = adapter
        }
        binding.spinnerHeight.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "centimeters (cm)" -> {
                        idealWeightCalculatorViewModel.changeToCentimeters()

                        binding.numberPicker.minValue = 0
                        binding.numberPicker.maxValue = AppArrays.numbersArrayCm.size - 1
                        binding.numberPicker.displayedValues = AppArrays.numbersArrayCm
                        numberPickerArray = AppArrays.numbersArrayCm
                    }

                    "meters (m)" -> {
                        idealWeightCalculatorViewModel.changeTometers()
                        binding.numberPicker.minValue = 0
                        binding.numberPicker.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPicker.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArray = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        idealWeightCalculatorViewModel.changeToFeets()
                        binding.numberPicker.minValue = 0
                        binding.numberPicker.maxValue = AppArrays.numbersArrayFeet.size - 1
                        binding.numberPicker.displayedValues = AppArrays.numbersArrayFeet
                        numberPickerArray = AppArrays.numbersArrayFeet

                    }

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun observeWeightUnit() {
        idealWeightCalculatorViewModel.measure.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsHeight.text = it
        })
    }

    private fun observerIdealWeightApiResponse() {
        viewModel.idealWeightResponse.observe(requireActivity(), Observer {
            height = "${it.data?.hamwi.toString()} kg"
            ToastUtil.makeToast(requireContext(), height)

        })
    }

    private fun callIdealWeightApi(gender: String, height: String) {
        viewModel.callIdealWeightApi(gender, height)
    }

    private fun observePorgress() {
        viewModel.showProgressIdealWeight.observe(requireActivity(), Observer {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
                binding.mainView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.mainView.visibility = View.VISIBLE
            }
        })
    }

    private fun observerErrorMessageApiResponse() {
        viewModel.errorMessage.observe(requireActivity(), Observer {
            ToastUtil.makeToast(requireContext(), it)
        })
    }
}