package com.herbalrecipe.herbrec.models

data class User(
    val userId: String,
    val userName: String,
    val userEmail: String,
    val userPassword: String, // Opsional, dipakai jika menggunakan fungsi untuk mengenkripsi password.
    val userFavorites: List<Recipe>,
)
