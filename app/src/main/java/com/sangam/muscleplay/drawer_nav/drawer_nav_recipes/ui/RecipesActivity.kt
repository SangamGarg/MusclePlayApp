package com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.databinding.ActivityRecepiesBinding
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.model.RecipesResponseModelItem
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.ui.adapter.RecipesAdapter
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.viewModel.RecipesViewModel

class RecipesActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRecepiesBinding.inflate(layoutInflater)
    }
    lateinit var viewModel: RecipesViewModel
    private var listOfData: ArrayList<RecipesResponseModelItem>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        HideStatusBarUtil.hideStatusBar(this)
        viewModel = ViewModelProvider(this)[RecipesViewModel::class.java]
        observeProgress()
        observerErrorMessageApiResponse()
        observerRecipesApiResponse()
        initListener()
    }


    private fun initListener() {
        binding.searchButton.setOnClickListener {
            val query = binding.searchEt.text.toString()

            if (query.trim().isEmpty()) {
                binding.searchLayout.isHelperTextEnabled = true
                binding.searchLayout.helperText = "*Required"
            } else {
                // Close the keyboard
                hideKeyboard()

                binding.searchLayout.isHelperTextEnabled = false
                callRecipesApi(query)
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
    }

    private fun callRecipesApi(query: String) {
        viewModel.callRecipesApi(query)
    }

    private fun observerRecipesApiResponse() {
        viewModel.recepiesResponse.observe(this, Observer {
            listOfData = it
            if (listOfData.isNullOrEmpty()) {
                binding.lottieAnimation.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.lottieAnimation.visibility = View.GONE
                val adapter = RecipesAdapter(this, listOfData!!)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = adapter

            }
        })
    }

    private fun observeProgress() {
        viewModel.showProgress.observe(this, Observer {
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
        viewModel.errorMessage.observe(this, Observer {
            ToastUtil.makeToast(this, it)
        })
    }
}