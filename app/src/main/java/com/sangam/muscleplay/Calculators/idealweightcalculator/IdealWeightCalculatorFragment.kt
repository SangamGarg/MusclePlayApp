package com.sangam.muscleplay.Calculators.idealweightcalculator

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
import com.sangam.muscleplay.botton_nav.home.viewmodel.HomeViewModel
import com.sangam.muscleplay.databinding.FragmentIdealWeightCalculatorBinding
import com.sangam.muscleplay.databinding.IdealWeightLayoutBottomSheetDialogBinding

class IdealWeightCalculatorFragment : Fragment() {
    private val binding by lazy {
        FragmentIdealWeightCalculatorBinding.inflate(layoutInflater)
    }
    private var numberPickerArray = emptyArray<String>()
    lateinit var viewModel: HomeViewModel
    lateinit var idealWeight: String
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

        binding.calculateIdealWeightButton.setOnClickListener {
            val height = AppConvertUnitsUtil.convertUnitHeight(
                selectedItem, numberPickerArray, binding.numberPickerHeight
            )
//            ToastUtil.makeToast(requireContext(), height)

            if (flag == 3) {
                ToastUtil.makeToast(requireContext(), "Please select a gender")
            } else {
                callIdealWeightApi(gender, height)
            }
        }
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
                selectedItem = parent?.getItemAtPosition(position).toString()
                when (selectedItem) {
                    "centimeters (cm)" -> {
                        idealWeightCalculatorViewModel.changeToCentimeters()

                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayCm.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayCm
                        numberPickerArray = AppArrays.numbersArrayCm
                    }

                    "meters (m)" -> {
                        idealWeightCalculatorViewModel.changeTometers()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayMeter.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayMeter
                        numberPickerArray = AppArrays.numbersArrayMeter

                    }

                    "feet (ft)" -> {
                        idealWeightCalculatorViewModel.changeToFeets()
                        binding.numberPickerHeight.minValue = 0
                        binding.numberPickerHeight.maxValue = AppArrays.numbersArrayFeet.size - 1
                        binding.numberPickerHeight.displayedValues = AppArrays.numbersArrayFeet
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
            val hamwi = "${it.data?.hamwi.toString()} kg"
            val miller = "${it.data?.miller.toString()} kg"
            val robinson = "${it.data?.robinson.toString()} kg"
            val devine = "${it.data?.devine.toString()} kg"
            showBottomDialog(hamwi, miller, robinson, devine)

        })
    }

    private fun showBottomDialog(hamwi: String, miller: String, robinson: String, devine: String) {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = IdealWeightLayoutBottomSheetDialogBinding.inflate(layoutInflater)
        view.close.setOnClickListener {
            dialog.dismiss()
        }
        view.hamwi.text = hamwi
        view.miller.text = miller
        view.robinson.text = robinson
        view.devine.text = devine

        view.imageViewShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            val message =
                "I measured my ideal weight using MusclePlay application and it comes out to be approx. $idealWeight"
            intent.putExtra(Intent.EXTRA_TEXT, message)
            requireContext().startActivity(intent)
        }

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(view.root)
        dialog.show()
//        val bottomDialogSheet = BmiBottomSheetFragment()
//        bottomDialogSheet.show(parentFragmentManager, "Bottom Sheet")
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