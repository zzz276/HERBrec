package com.herbalrecipe.herbrec.models

import androidx.lifecycle.LiveData
import androidx.room3.Dao
import androidx.room3.Entity
import androidx.room3.Insert
import androidx.room3.PrimaryKey
import androidx.room3.Query

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey val commentId: String,
    val userName: String,
    val commentDesc: String,
    val timestamp: String = "3 Juli 2026, 13:25"
)

@Dao
interface CommentDao {
    @Insert
    suspend fun insertComment(comment: Comment)
    @Query("SELECT * FROM comments ORDER BY timestamp DESC")
    suspend fun getAllComments(): LiveData<List<Comment>>
}
