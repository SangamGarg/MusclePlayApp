package com.sangam.muscleplay.Calculators.bmicalculator

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
import com.sangam.muscleplay.R
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentBmiBinding
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding


class BmiCalculatorFragment : Fragment() {

    private val binding by lazy {
        FragmentBmiBinding.inflate(layoutInflater)
    }
    lateinit var bmiViewModel: BmiViewModel
    private var numberPickerArrayWeight = emptyArray<String>()
    private var numberPickerArrayHeight = emptyArray<String>()
    lateinit var viewModel: HomeViewModel
    lateinit var height: String
    lateinit var bmi: String
    lateinit var health: String
    lateinit var weight: String
    lateinit var gender: String
    lateinit var age: String
    private var flag = 3
    lateinit var selectedItemHeight: String
    lateinit var selectedItemWeight: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)
        bmiViewModel = ViewModelProvider(this)[BmiViewModel::class.java]
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initListener()

        setText()
        observeWeightUnit()
        observeHeightUnit()
        observeProgress()
        observerErrorMessageApiResponse()
        observerBmiApiResponse()


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

        binding.calculateBmiButton.setOnClickListener {
            val height = AppConvertUnitsUtil.convertUnitHeight(
                selectedItemHeight, numberPickerArrayHeight, binding.numberPickerHeight
            )
            val weight = AppConvertUnitsUtil.convertUnitWeight(
                selectedItemWeight, numberPickerArrayWeight, binding.numberPickerWeight
            )
            val age = binding.tvAgeLive.text.toString()

            if (flag == 3) {
                ToastUtil.makeToast(requireContext(), "Please select a gender")
            } else {
                callBmiApi(age, weight, height)
            }
        }

        binding.decrement.setOnClickListener {
            if (bmiViewModel.age == 1) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
            } else {
                bmiViewModel.decrement()
                setText()
            }
        }
        binding.increment.setOnClickListener {
            if (bmiViewModel.age == 80) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
            } else {
                bmiViewModel.increment()
                setText()
            }
        }

        spinnerHeight()
        spinnerWeight()

    }

    private fun spinnerWeight() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_weight_measurements,
            android.R.layout.simple_spinner_item
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
                        bmiViewModel.changeToKilograms()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayKg.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayKg
                        numberPickerArrayWeight = AppArrays.numbersArrayKg

                    }

                    "pound (lbs)" -> {
                        bmiViewModel.changeToPounds()
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
                selectedItemHeight = parent?.getItemAtPosition(position).toString()
                when (selectedItemHeight) {
                    "centimeters (cm)" -> {
                        bmiViewModel.changeToCentimeters()

                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayCm.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayCm
                        numberPickerArrayHeight = AppArrays.numbersArrayCm
                    }

                    "meters (m)" -> {
                        bmiViewModel.changeTometers()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArrayHeight = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        bmiViewModel.changeToFeets()
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

    fun setText() {
        binding.tvAgeLive.text = bmiViewModel.age.toString()
    }

    private fun showBottomDialog() {
        val dialog = BottomSheetDialog(requireContext())
        val view = FragmentBmiBottomSheetBinding.inflate(layoutInflater)
        view.close.setOnClickListener {
            dialog.dismiss()
        }
        view.tvBmiValue.text = bmi
        view.tvHealthValue.text = health
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(view.root)
        dialog.show()
//        val bottomDialogSheet = BmiBottomSheetFragment()
//        bottomDialogSheet.show(parentFragmentManager, "Bottom Sheet")
    }

    private fun observeWeightUnit() {
        bmiViewModel.measureWeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsWeight.text = it
        })
    }

    private fun observeHeightUnit() {
        bmiViewModel.measureHeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsHeight.text = it
        })
    }

    private fun observerBmiApiResponse() {
        viewModel.bmiResponse.observe(requireActivity(), Observer {
            bmi = it.data?.bmi.toString()
            health = it.data?.health.toString()
            showBottomDialog()
        })
    }

    private fun callBmiApi(age: String, weight: String, height: String) {
        viewModel.callBmiApi(age, weight, height)
    }

    private fun observeProgress() {
        viewModel.showProgressBmi.observe(requireActivity(), Observer {
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