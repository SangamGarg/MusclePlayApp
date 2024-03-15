package com.sangam.muscleplay.Calculators.dailycaloriescalculator

import android.content.Intent
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
import com.sangam.muscleplay.botton_nav.home.model.DailyCalorieGoals
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.DailyCaloriesBottomSheetDialogBinding
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding
import com.sangam.muscleplay.databinding.FragmentDailycaloriesCalculatorBinding

class DailyCaloriesCalculatorFragment : Fragment() {
    private val binding by lazy {
        FragmentDailycaloriesCalculatorBinding.inflate(layoutInflater)
    }
    lateinit var dailyCaloriesViewModel: DailyCaloriesViewModel
    private var numberPickerArrayWeight = emptyArray<String>()
    private var numberPickerArrayHeight = emptyArray<String>()
    lateinit var viewModel: HomeViewModel
    lateinit var selectedItemHeight: String
    lateinit var selectedItemWeight: String
    lateinit var gender: String
    private var flag = 3
    lateinit var activityLevel: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dailycalories_calculator, container, false)
        dailyCaloriesViewModel = ViewModelProvider(this)[DailyCaloriesViewModel::class.java]
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initListener()

        setText()
        observeWeightUnit()
        observeHeightUnit()
        observeProgress()
        observerErrorMessageApiResponse()
        observerDailyCaloriesApiResponse()


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


        binding.calculateDailyCaloriesButton.setOnClickListener {
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
                callDailyCaloriesApi(age, weight, height, activityLevel)
            }
        }

        binding.decrement.setOnClickListener {
            if (dailyCaloriesViewModel.age == 1) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
            } else {
                dailyCaloriesViewModel.decrement()
                setText()
            }
        }
        binding.increment.setOnClickListener {
            if (dailyCaloriesViewModel.age == 80) {
                ToastUtil.makeToast(requireContext(), "Maximum Value Achieved")
            } else {
                dailyCaloriesViewModel.increment()
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
                        dailyCaloriesViewModel.changeToKilograms()
                        binding.numberPickerWeight.minValue = 0
                        binding.numberPickerWeight.maxValue = AppArrays.numbersArrayKg.size - 1
                        binding.numberPickerWeight.displayedValues = AppArrays.numbersArrayKg
                        numberPickerArrayWeight = AppArrays.numbersArrayKg

                    }

                    "pound (lbs)" -> {
                        dailyCaloriesViewModel.changeToPounds()
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
                        dailyCaloriesViewModel.changeToCentimeters()

                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayCm.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayCm
                        numberPickerArrayHeight = AppArrays.numbersArrayCm
                    }

                    "meters (m)" -> {
                        dailyCaloriesViewModel.changeTometers()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArrayHeight = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        dailyCaloriesViewModel.changeToFeets()
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
        binding.tvAgeLive.text = dailyCaloriesViewModel.age.toString()
    }

    private fun showBottomDialog(bmr: Double?, goals: DailyCalorieGoals?) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = DailyCaloriesBottomSheetDialogBinding.inflate(layoutInflater)
        view.close.setOnClickListener {
            dialog.dismiss()
        }
        view.bmr.text = "BMR: $bmr"
        view.maintainWeight.text = String.format("%.2f", goals?.maintainweight) + " cal"
        view.mildWeightGain.text = String.format("%.2f", goals?.weightGain?.calory) + " cal"
        view.weightGain.text = String.format("%.2f", goals?.mildWeightGain?.calory) + " cal"
        view.extremeWeightGain.text =
            String.format("%.2f", goals?.extremeWeightGain?.calory) + " cal"
        view.mildWeightLoss.text = String.format("%.2f", goals?.weightLoss?.calory) + " cal"
        view.weightLoss.text = String.format("%.2f", goals?.mildWeightLoss?.calory) + " cal"
        view.extremeWeightLoss.text =
            String.format("%.2f", goals?.extremeWeightLoss?.calory) + " cal"



        view.imageViewShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message =
                "I measured my daily calories intake using MusclePlay application and it comes out is ${goals?.maintainweight.toString()}"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            requireContext().startActivity(intent)
        }
        var flag = 0
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(view.root)
        dialog.show()
//        val bottomDialogSheet = BmiBottomSheetFragment()
//        bottomDialogSheet.show(parentFragmentManager, "Bottom Sheet")
    }

    private fun observeWeightUnit() {
        dailyCaloriesViewModel.measureWeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsWeight.text = it
        })
    }

    private fun observeHeightUnit() {
        dailyCaloriesViewModel.measureHeight.observe(viewLifecycleOwner, Observer {
            binding.tvUnitsHeight.text = it
        })
    }

    private fun observerDailyCaloriesApiResponse() {
        viewModel.dailyCaloriesResponse.observe(requireActivity(), Observer {

            showBottomDialog(it.data?.BMR, it.data?.goals)
        })
    }

    private fun callDailyCaloriesApi(
        age: String, weight: String, height: String, activity_level: String
    ) {
        viewModel.callDailyCaloriesApi(age, gender, height, weight, activity_level)
    }

    private fun observeProgress() {
        viewModel.showProgressDalyCalories.observe(requireActivity(), Observer {
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