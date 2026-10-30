package com.herbalrecipe.herbrec.models

import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val recipeId: String,
    val recipeTitle: String,
    val recipeDesc: String,
    val recipePict: String,
    val recipeComments: List<Comment>,
)

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)
    @Query("SELECT * FROM recipes ORDER BY timestamp DESC")
    suspend fun getAllRecipes(): LiveData<List<Recipe>>
}
