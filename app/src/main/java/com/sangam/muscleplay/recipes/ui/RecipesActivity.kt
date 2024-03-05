package com.sangam.muscleplay.recipes.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangam.muscleplay.AppUtils.HideStatusBarUtil
import com.sangam.muscleplay.AppUtils.ToastUtil
import com.sangam.muscleplay.databinding.ActivityRecepiesBinding
import com.sangam.muscleplay.recipes.model.RecipesResponseModelItem
import com.sangam.muscleplay.recipes.ui.adapter.RecipesAdapter
import com.sangam.muscleplay.recipes.viewModel.RecipesViewModel

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
        callRecipesApi("mushroomnrisotto")
    }

    private fun callRecipesApi(query: String) {
        viewModel.callRecipesApi(query)
    }

    private fun observerRecipesApiResponse() {
        viewModel.recepiesResponse.observe(this, Observer {
            listOfData = it
            if (listOfData.isNullOrEmpty()) {
                binding.mainView.visibility = View.GONE
                binding.lottieAnimation.visibility = View.VISIBLE

            } else {
                val adapter = RecipesAdapter(this, listOfData!!)
                binding.recyclerView.layoutManager = LinearLayoutManager(this)
                binding.recyclerView.adapter = adapter

            }
        })
    }

    private fun observeProgress() {
        viewModel.showProgress.observe(this, Observer {
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
        viewModel.errorMessage.observe(this, Observer {
            ToastUtil.makeToast(this, it)
        })
    }
}