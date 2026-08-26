package com.herbalrecipe.herbrec.models

data class Recipe(
    val recipeId: String,
    val recipeTitle: String,
    val recipeDesc: String,
    val recipePict: String,
    val recipeComments: List<Comment>,
)
