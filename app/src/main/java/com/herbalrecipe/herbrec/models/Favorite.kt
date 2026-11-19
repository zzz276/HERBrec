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

@Entity(tableName = "favorites")
@Serializable
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "favorite_id")
    @SerialName("favorite_id")
    val favoriteId: String = "",

    @ColumnInfo(name = "created_at")
    @SerialName("created_at")
    val createdAt: String? = null,

    @ColumnInfo(name = "recipe_id")
    @SerialName("recipe_id")
    val recipeId: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = ""
)

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(favorites: List<Favorite>)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("SELECT * FROM favorites WHERE user_id = :userId")
    suspend fun getFavoritesForUser(userId: String): List<Favorite>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE user_id = :userId AND recipe_id = :recipeId LIMIT 1)")
    suspend fun isFavorite(userId: String, recipeId: String): Boolean
}
