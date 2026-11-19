package com.herbalrecipe.herbrec.clients

import com.herbalrecipe.herbrec.models.AppDatabase
import com.herbalrecipe.herbrec.models.Comment
import com.herbalrecipe.herbrec.models.Favorite
import com.herbalrecipe.herbrec.models.Recipe
import com.herbalrecipe.herbrec.models.User
import io.github.jan.supabase.auth.Auth
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
    return SupabaseInit.supabase.from("recipes").select(columns = Columns.list("recipe_id, recipe_title, recipe_ingredients, recipe_steps, recipe_serves")).decodeList<Recipe>()
}

suspend fun fetchAllUsers(): List<User> {
    return SupabaseInit.supabase.from("users").select().decodeList<User>()
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

suspend fun syncRecipesToRoom(db: AppDatabase): List<Recipe> {
    val recipes = fetchAllRecipes()
    if (recipes.isNotEmpty()) {
        db.recipeDao().insertRecipes(recipes)
    }
    return recipes
}

suspend fun syncUsersToRoom(db: AppDatabase): List<User> {
    val users = fetchAllUsers()
    if (users.isNotEmpty()) {
        db.userDao().insertUsers(users)
    }
    return users
}
