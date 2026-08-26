package com.herbalrecipe.herbrec.ui.recipe

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.herbalrecipe.herbrec.models.Recipe

class RecipeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val recipeLiveData = MutableLiveData<List<Recipe>>()


}