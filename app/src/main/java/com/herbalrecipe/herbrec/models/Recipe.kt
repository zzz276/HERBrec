package com.herbalrecipe.herbrec.models

import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "recipes")
@Serializable
data class Recipe(
    @PrimaryKey
    @SerialName("recipe_id")
    val recipeId: String = "",

    @SerialName("recipe_title")
    val recipeTitle: String = "",

    @SerialName("recipe_ingredients")
    val recipeIngredients: String = "",

    @SerialName("recipe_steps")
    val recipeSteps: String = "",

    @SerialName("recipe_serves")
    val recipeServes: String = "",

//    @SerialName("recipe_pict")
//    val recipePict: String? = null,
)

@Dao
interface RecipeDao {
    @Insert
    suspend fun insertRecipe(recipe: Recipe)
    @Query("SELECT * FROM recipes ORDER BY timestamp DESC")
    suspend fun getAllRecipes(): LiveData<List<Recipe>>
}
