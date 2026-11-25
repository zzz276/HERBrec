package com.herbalrecipe.herbrec.clients

import com.herbalrecipe.herbrec.models.AppDatabase
import com.herbalrecipe.herbrec.models.Comment
import com.herbalrecipe.herbrec.models.Favorite
import com.herbalrecipe.herbrec.models.Recipe
import com.herbalrecipe.herbrec.models.User
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.OtpType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns

object SupabaseInit {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://pjilceskkcflhjuzciig.supabase.co",
        supabaseKey = "sb_publishable__PG6UDERMWiUPgZYLZZLPQ_v7JSkCfg"
    ) {
        install(Auth)
        install(Postgrest)
    }
}

suspend fun fetchAllRecipes(): List<Recipe> {
    return SupabaseInit.supabase.from("recipes").select().decodeList<Recipe>()
}

suspend fun fetchAllUsers(): List<User> {
    return SupabaseInit.supabase.from("users").select().decodeList<User>()
}

suspend fun fetchAllComments(): List<Comment> {
    return SupabaseInit.supabase.from("comments").select().decodeList<Comment>()
}

suspend fun fetchAllFavorites(): List<Favorite> {
    return SupabaseInit.supabase.from("favorites").select().decodeList<Favorite>()
}

suspend fun fetchCommentsForRecipe(recipeId: String): List<Comment> {
    return SupabaseInit.supabase.from("comments").select {
        filter {
            eq("recipe_id", recipeId)
        }
    }.decodeList<Comment>()
}

suspend fun fetchFavoritesForUser(userId: String): List<Favorite> {
    return SupabaseInit.supabase.from("favorites").select {
        filter {
            eq("user_id", userId)
        }
    }.decodeList<Favorite>()
}

suspend fun syncToRoom(db: AppDatabase) {
    val users = fetchAllUsers()
    val recipes = fetchAllRecipes()
    val comments = fetchAllComments()
    val favorites = fetchAllFavorites()

    if (users.isNotEmpty()) { db.userDao().insertUsers(users) }
    if (recipes.isNotEmpty()) { db.recipeDao().insertRecipes(recipes) }
    if (comments.isNotEmpty()) { db.commentDao().insertComments(comments) }
    if (favorites.isNotEmpty()) { db.favoriteDao().insertFavorites(favorites) }
}

suspend fun signIn(email: String, password: String) {
    SupabaseInit.supabase.auth.signInWith(Email) {
        this.email = email
        this.password = password
    }
}

suspend fun signUp(email: String, password: String) {
    SupabaseInit.supabase.auth.signUpWith(Email) {
        this.email = email
        this.password = password
    }
}
