package com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.model.RecipesResponseModel
import com.sangam.muscleplay.drawer_nav.drawer_nav_recipes.repository.RecipesRepository

class RecipesViewModel : ViewModel() {
    var showProgress = MutableLiveData<Boolean>()
    var errorMessage = MutableLiveData<String>()
    var recepiesResponse = MutableLiveData<RecipesResponseModel>()
    private val repository = RecipesRepository()

    init {
        this.recepiesResponse = repository.recipesResponse
        this.showProgress = repository.showProgress
        this.errorMessage = repository.errorMessage
    }

    fun callRecipesApi(query: String?) {
        repository.recipesResponse(query)
    }
}