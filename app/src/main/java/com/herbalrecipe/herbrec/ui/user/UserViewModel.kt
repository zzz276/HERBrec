package com.herbalrecipe.herbrec.ui.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.herbalrecipe.herbrec.models.User

class UserViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val userLiveData = MutableLiveData<List<User>>()


}