package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.ui

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
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.model.BurnedCaloriesFromActivityModelItem
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.ui.adapter.BurnedCaloriesFromActivityAdapter
import com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.viewModel.BurnedCaloriesFromActivityViewModel
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.model.CaloriesInFoodItem
import com.sangam.muscleplay.Calculators.caloryinfoodcalculator.ui.adapter.CaloriesInFoodAdapter
import com.sangam.muscleplay.R
import com.sangam.muscleplay.databinding.FragmentBurnedCaloriesFromActivityBinding

class BurnedCaloriesFromActivityFragment : Fragment() {
    private val binding by lazy {
        FragmentBurnedCaloriesFromActivityBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: BurnedCaloriesFromActivityViewModel
    private var listOfData: ArrayList<BurnedCaloriesFromActivityModelItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[BurnedCaloriesFromActivityViewModel::class.java]
        val view =
            inflater.inflate(R.layout.fragment_burned_calories_from_activity, container, false)
        observeProgress()
        observerErrorMessageApiResponse()
        observerBurnedCaloriesFromActivityResponse()
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
                callBurnedCaloriesFromActivityApi(query)
            }
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
    }

    private fun callBurnedCaloriesFromActivityApi(activity: String) {
        viewModel.callBurnedCaloriesFromActivityApi(activity)
    }

    private fun observerBurnedCaloriesFromActivityResponse() {
        viewModel.burnedCaloriesFromActivityResponse.observe(requireActivity(), Observer {
            listOfData = it
            if (listOfData.isNullOrEmpty()) {
                binding.lottieAnimation.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.lottieAnimation.visibility = View.GONE

                val adapter = BurnedCaloriesFromActivityAdapter(requireContext(), listOfData!!)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter

            }
        })
    }

    private fun observerErrorMessageApiResponse() {
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            ToastUtil.makeToast(requireContext(), it)
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


}