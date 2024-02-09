package com.sangam.muscleplay.Calculators.bmicalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.bmicalculator.bottomsheetfragment.BmiBottomSheetFragment
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentBmiBinding
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding


class BmiCalculatorFragment : Fragment() {

    private val binding by lazy {
        FragmentBmiBinding.inflate(layoutInflater)
    }
    lateinit var bmiViewModel: BmiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)
        bmiViewModel = ViewModelProvider(this).get(BmiViewModel::class.java)
        binding.calculateBmiButton.setOnClickListener {
            showBottomDialog()
        }
        setText()

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
        return binding.root
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
        dialog.setContentView(view.root)
        dialog.show()
//        val bottomDialogSheet = BmiBottomSheetFragment()
//        bottomDialogSheet.show(parentFragmentManager, "Bottom Sheet")
    }

}