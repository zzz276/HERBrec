package com.herbalrecipe.herbrec.clients

import com.herbalrecipe.herbrec.models.Recipe
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
        //install other modules
    }
}

suspend fun fetchAllRecipes(): List<Recipe> {
    return SupabaseInit.supabase.from("recipes").select(columns = Columns.list("recipe_id, recipe_title, recipe_ingredients, recipe_steps, recipe_serves")).decodeList<Recipe>()
}
