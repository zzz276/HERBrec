package com.herbalrecipe.herbrec.models

data class Comment(
    val commentId: String,
    val userName: String,
    val commentDesc: String,
    val timestamp: String = "3 Juli 2026, 13:25"
)
