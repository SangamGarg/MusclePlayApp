package com.sangam.muscleplay.recipes.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.recipes.model.RecipesResponseModel
import com.sangam.muscleplay.recipes.repository.RecepiesRepository

class RecipesViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var recepiesResponse = MutableLiveData<RecipesResponseModel>()
    private val repository = RecepiesRepository()

    init {
        this.recepiesResponse = repository.recipesResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callRecipesApi(query: String?) {
        repository.recipesResponse(query)
    }
}