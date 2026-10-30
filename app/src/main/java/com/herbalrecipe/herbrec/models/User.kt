package com.herbalrecipe.herbrec.models

import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import androidx.room3.Update

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String,
    val userName: String,
    val userEmail: String, // Opsional?
    val userPassword: String, // Opsional, dipakai jika menggunakan fungsi untuk mengenkripsi password.
    val userFavorites: List<Recipe>,
)

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): LiveData<List<User>>
    @Update
    suspend fun updateUser(user: User)
}
