package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodItem
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui.adapter.CaloriesInFoodAdapter
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.viewModel.CaloriesInFoodViewModel
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentCaloriesInFoodCalculatorBinding


class CaloriesInFoodCalculatorFragment : Fragment() {
    private val binding by lazy {
        FragmentCaloriesInFoodCalculatorBinding.inflate(layoutInflater)
    }
    lateinit var viewModel: CaloriesInFoodViewModel
    private var listOfData: ArrayList<CaloriesInFoodItem>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[CaloriesInFoodViewModel::class.java]

        val view = inflater.inflate(R.layout.fragment_calories_in_food_calculator, container, false)
        observeProgress()
        observerErrorMessageApiResponse()
        observerCaloriesInFoodApiResponse()
        initListener()
        return binding.root
    }

    private fun initListener() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchEt.text.toString()

            if (query.isEmpty()) {
                binding.searchLayout.isHelperTextEnabled = true
                binding.searchLayout.helperText = "*Required"
            } else {
                // Close the keyboard
                hideKeyboard()

                binding.searchLayout.isHelperTextEnabled = false
                callCaloriesInfoodApi(query)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
    }

    private fun callCaloriesInfoodApi(query: String) {
        viewModel.callCaloriesInFoodApi(query)
    }

    private fun observerCaloriesInFoodApiResponse() {
        viewModel.caloriesInFoodResponse.observe(requireActivity(), Observer {
            listOfData = it.items
            if (listOfData.isNullOrEmpty()) {
                binding.lottieAnimation.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.lottieAnimation.visibility = View.GONE
                val adapter = CaloriesInFoodAdapter(requireContext(), listOfData!!) {}
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter

            }
        })
    }

    private fun observeProgress() {
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.lottieAnimation.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
    }

    private fun observerErrorMessageApiResponse() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            ToastUtil.makeToast(requireContext(), it)
        })
    }
}