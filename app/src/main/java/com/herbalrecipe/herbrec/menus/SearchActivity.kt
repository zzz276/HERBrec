package com.herbalrecipe.herbrec.menus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herbalrecipe.herbrec.CommentActivity
import com.herbalrecipe.herbrec.R
import com.herbalrecipe.herbrec.ViewRecipeActivity
import com.herbalrecipe.herbrec.models.AppDatabase
import com.herbalrecipe.herbrec.models.Recipe
import com.herbalrecipe.herbrec.ui.components.AppBar
import com.herbalrecipe.herbrec.ui.components.BottomNavigationBar
import com.herbalrecipe.herbrec.ui.components.NavScreen
import com.herbalrecipe.herbrec.ui.components.RecipeCard
import com.herbalrecipe.herbrec.ui.components.formatDateString
import com.herbalrecipe.herbrec.ui.theme.DarkMaroon
import com.herbalrecipe.herbrec.ui.theme.HERBrecTheme
import com.herbalrecipe.herbrec.ui.theme.RedAccent
import com.herbalrecipe.herbrec.ui.theme.RedBackground
import com.herbalrecipe.herbrec.ui.theme.White

class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HERBrecTheme {
                SearchScreen()
            }
        }
    }
}

@Composable
fun SearchScreen() {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<Recipe>() }
    val userNamesMap = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(searchQuery) {
        val db = AppDatabase.getInstance(context.applicationContext)
        try {
            if (searchQuery.isBlank()) {
                searchResults.clear()
            } else {
                val results = db.recipeDao().getRecipesByTitle(searchQuery.trim())
                searchResults.clear()
                searchResults.addAll(results)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            searchResults.clear()
        }
    }

    LaunchedEffect(searchResults.size) {
        if (searchResults.isNotEmpty()) {
            val db = AppDatabase.getInstance(context.applicationContext)
            searchResults.forEach { recipe ->
                if (recipe.userId.isNotBlank() && !userNamesMap.containsKey(recipe.userId)) {
                    val user = db.userDao().getUserById(recipe.userId)
                    if (user != null && user.userName.isNotBlank()) {
                        userNamesMap[recipe.userId] = user.userName
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(currentScreen = NavScreen.SEARCH) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(White)
        ) {
            // Top App Bar Header
            AppBar("HERBrec")
            Spacer(modifier = Modifier.height(20.dp))

            // Search Bar Input Field
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(52.dp)
                    .background(
                        color = RedBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 36.dp),
                    textStyle = TextStyle(
                        color = DarkMaroon,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    ),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari",
                                style = TextStyle(
                                    color = Color(0xFFAC7C7C),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontFamily = FontFamily.Serif
                                )
                            )
                        }
                        innerTextField()
                    }
                )

                Icon(
                    painter = painterResource(id = R.drawable.search_24),
                    contentDescription = "Search",
                    tint = RedAccent,
                    modifier = Modifier
                        .size(30.dp)
                        .align(Alignment.CenterEnd)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search Results or Empty No-Result Message
            if (searchQuery.isNotBlank() && searchResults.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Resep tidak ditemukan",
                        style = TextStyle(
                            color = DarkMaroon,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    )
                }
            } else if (searchResults.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(searchResults) { recipe ->
                        val authorName = userNamesMap[recipe.userId] ?: recipe.userId
                        RecipeCard(
                            recipeTitle = recipe.recipeTitle,
                            userName = authorName,
                            onDetailClick = {
                                val intent = Intent(context, ViewRecipeActivity::class.java).apply {
                                    putExtra("EXTRA_RECIPE_ID", recipe.recipeId)
                                    putExtra("EXTRA_RECIPE_TITLE", recipe.recipeTitle)
                                    putExtra("EXTRA_USER_ID", recipe.userId)
                                    putExtra("EXTRA_USERNAME", authorName)
                                    putExtra("EXTRA_CREATED_DATE", formatDateString(recipe.createdAt))
                                    putExtra("EXTRA_RECIPE_INGREDIENTS", recipe.recipeIngredients)
                                    putExtra("EXTRA_RECIPE_STEPS", recipe.recipeSteps)
                                    putExtra("EXTRA_RECIPE_SERVES", recipe.recipeServes)
                                }
                                context.startActivity(intent)
                            },
                            onCommentClick = {
                                val intent = Intent(context, CommentActivity::class.java).apply {
                                    putExtra("EXTRA_RECIPE_ID", recipe.recipeId)
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    HERBrecTheme {
        SearchScreen()
    }
}
