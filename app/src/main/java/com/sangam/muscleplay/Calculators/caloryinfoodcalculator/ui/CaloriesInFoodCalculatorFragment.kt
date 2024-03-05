package com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        callCaloriesInfoodApi("parrot")
    }

    private fun callCaloriesInfoodApi(query: String) {
        viewModel.callCaloriesInFoodApi(query)
    }

    private fun observerCaloriesInFoodApiResponse() {
        viewModel.caloriesInFoodResponse.observe(requireActivity(), Observer {
            listOfData = it.items
            if (listOfData.isNullOrEmpty()) {
                binding.mainView.visibility = View.GONE
                binding.lottieAnimation.visibility = View.VISIBLE

            } else {
                val adapter = CaloriesInFoodAdapter(requireContext(), listOfData!!) {

                }
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter

            }
        })
    }

    private fun observeProgress() {
        viewModel.showProgress.observe(viewLifecycleOwner, Observer {
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
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            ToastUtil.makeToast(requireContext(), it)
        })
    }
}