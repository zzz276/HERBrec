package com.herbalrecipe.herbrec.models

import androidx.room3.ColumnInfo
import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.PrimaryKey
import androidx.room3.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "recipes")
@Serializable
data class Recipe(
    @PrimaryKey
    @ColumnInfo(name = "recipe_id")
    @SerialName("recipe_id")
    val recipeId: String = "",

    @ColumnInfo(name = "created_at")
    @SerialName("created_at")
    val createdAt: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = "",

    @ColumnInfo(name = "recipe_title")
    @SerialName("recipe_title")
    val recipeTitle: String = "",

    @ColumnInfo(name = "recipe_ingredients")
    @SerialName("recipe_ingredients")
    val recipeIngredients: String = "",

    @ColumnInfo(name = "recipe_steps")
    @SerialName("recipe_steps")
    val recipeSteps: String = "",

    @ColumnInfo(name = "recipe_serves")
    @SerialName("recipe_serves")
    val recipeServes: String = "",

    @ColumnInfo(name = "recipe_picture")
    @SerialName("recipe_picture")
    val recipePicture: String? = null
)

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: Recipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes ORDER BY created_at DESC, recipe_id DESC")
    suspend fun getAllRecipes(): List<Recipe>

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipeId")
    suspend fun getRecipeById(recipeId: String): Recipe?

    @Query("SELECT * FROM recipes WHERE recipe_title LIKE '%' || :title || '%'")
    suspend fun getRecipesByTitle(title: String): List<Recipe>

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}
