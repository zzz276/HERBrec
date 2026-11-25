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

@Entity(tableName = "comments")
@Serializable
data class Comment(
    @PrimaryKey
    @ColumnInfo(name = "comment_id")
    @SerialName("comment_id")
    val commentId: String = "",

    @ColumnInfo(name = "created_at")
    @SerialName("created_at")
    val createdAt: String = "",

    @ColumnInfo(name = "recipe_id")
    @SerialName("recipe_id")
    val recipeId: String = "",

    @ColumnInfo(name = "user_id")
    @SerialName("user_id")
    val userId: String = "",

    @ColumnInfo(name = "comment_desc")
    @SerialName("comment_desc")
    val commentDesc: String = "",

    // UI display fallback for username
    @ColumnInfo(name = "user_name")
    val userName: String = "",
)

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<Comment>)

    @Query("SELECT * FROM comments WHERE recipe_id = :recipeId")
    suspend fun getCommentsForRecipe(recipeId: String): List<Comment>

    @Query("SELECT * FROM comments")
    suspend fun getAllComments(): List<Comment>

    @Delete
    suspend fun deleteComment(comment: Comment)
}
