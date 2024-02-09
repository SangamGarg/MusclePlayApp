package com.sangam.muscleplay.Calculators.bmicalculator.bottomsheetfragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentBmiBottomSheetBinding

class BmiBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding by lazy {
        FragmentBmiBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi_bottom_sheet, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.GREEN))
        binding.close.setOnClickListener {
            dismiss()
        }
        return binding.root

    }
}