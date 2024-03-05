package com.sangam.muscleplay.Calculators.burnedcaloriesfromactivitycalculator.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        callBurnedCaloriesFromActivityApi("running")
    }

    private fun callBurnedCaloriesFromActivityApi(activity: String) {
        viewModel.callBurnedCaloriesFromActivityApi(activity)
    }

    private fun observerBurnedCaloriesFromActivityResponse() {
        viewModel.burnedCaloriesFromActivityResponse.observe(requireActivity(), Observer {
            listOfData = it
            if (listOfData.isNullOrEmpty()) {
                binding.mainView.visibility = View.GONE
                binding.lottieAnimation.visibility = View.VISIBLE
            } else {
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
                binding.progressBar.visibility = View.VISIBLE
                binding.mainView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.mainView.visibility = View.VISIBLE
            }
        })
    }


}