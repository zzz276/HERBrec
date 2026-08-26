package com.herbalrecipe.herbrec.ui.comment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.herbalrecipe.herbrec.models.Comment

class CommentViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val commentLiveData = MutableLiveData<List<Comment>>()


}