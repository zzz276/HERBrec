package com.herbalrecipe.herbrec.menus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.herbalrecipe.herbrec.CommentActivity
import com.herbalrecipe.herbrec.CreateRecipeActivity
import com.herbalrecipe.herbrec.R
import com.herbalrecipe.herbrec.ViewRecipeActivity
import com.herbalrecipe.herbrec.clients.fetchAllRecipes
import com.herbalrecipe.herbrec.clients.syncRecipesToRoom
import com.herbalrecipe.herbrec.models.AppDatabase
import com.herbalrecipe.herbrec.models.Recipe
import com.herbalrecipe.herbrec.ui.components.AppBar
import com.herbalrecipe.herbrec.ui.components.BottomNavigationBar
import com.herbalrecipe.herbrec.ui.components.NavScreen
import com.herbalrecipe.herbrec.ui.components.RecipeCard
import com.herbalrecipe.herbrec.ui.theme.Black
import com.herbalrecipe.herbrec.ui.theme.BlackCharcoal
import com.herbalrecipe.herbrec.ui.theme.GreenSupport
import com.herbalrecipe.herbrec.ui.theme.HERBrecTheme
import com.herbalrecipe.herbrec.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeActivity : ComponentActivity() {
    private val loadedRecipes = mutableStateListOf<Recipe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true

        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            try {
                val fetched = fetchAllRecipes()
                if (fetched.isNotEmpty()) {
                    syncRecipesToRoom(db, fetched)
                }
                val local = db.recipeDao().getAllRecipes()
                withContext(Dispatchers.Main) {
                    loadedRecipes.clear()
                    loadedRecipes.addAll(local)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val local = db.recipeDao().getAllRecipes()
                withContext(Dispatchers.Main) {
                    loadedRecipes.clear()
                    loadedRecipes.addAll(local)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    keepSplashScreen = false
                }
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HERBrecTheme {
                HomeScreen(loadedRecipes)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        refreshRecipes()
    }

    private fun refreshRecipes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getInstance(applicationContext)
            try {
                val fetched = fetchAllRecipes()
                if (fetched.isNotEmpty()) {
                    syncRecipesToRoom(db, fetched)
                }
                val local = db.recipeDao().getAllRecipes()
                withContext(Dispatchers.Main) {
                    loadedRecipes.clear()
                    loadedRecipes.addAll(local)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                val local = db.recipeDao().getAllRecipes()
                withContext(Dispatchers.Main) {
                    loadedRecipes.clear()
                    loadedRecipes.addAll(local)
                }
            }
        }
    }
}

@Composable
fun HomeScreen(initialRecipes: List<Recipe> = emptyList()) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val recipes = remember { mutableStateListOf<Recipe>() }
    var isLoading by remember { mutableStateOf(initialRecipes.isEmpty()) }

    LaunchedEffect(initialRecipes.size) {
        if (initialRecipes.isNotEmpty()) {
            recipes.clear()
            recipes.addAll(initialRecipes)
            isLoading = false
        } else {
            isLoading = true
            try {
                val db = AppDatabase.getInstance(context.applicationContext)
                val local = db.recipeDao().getAllRecipes()
                recipes.clear()
                recipes.addAll(local)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(currentScreen = NavScreen.HOME) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .background(White)
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                AppBar("HERBrec")

                // Recipe Cards List
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    if (isLoading && recipes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) { CircularProgressIndicator(color = GreenSupport) }
                    } else if (recipes.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Belum ada resep.",
                                style = TextStyle(
                                    color = Black,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    } else {
                        recipes.forEach { recipe ->
                            RecipeCard(
                                recipeTitle = recipe.recipeTitle,
                                userName = recipe.userId,
                                onDetailClick = {
                                    val intent = Intent(context, ViewRecipeActivity::class.java).apply {
                                        putExtra("EXTRA_RECIPE_ID", recipe.recipeId)
                                        putExtra("EXTRA_RECIPE_TITLE", recipe.recipeTitle)
                                        putExtra("EXTRA_USER_ID", recipe.userId)
                                        putExtra("EXTRA_CREATED_DATE", com.herbalrecipe.herbrec.ui.components.formatDateString(recipe.createdAt))
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

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Floating Action Button (+)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF65B884), Color(0xFF417855))
                        )
                    )
                    .border(
                        border = BorderStroke(1.5.dp, Black),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        val intent = Intent(context, CreateRecipeActivity::class.java)
                        context.startActivity(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plus),
                    contentDescription = "Create Recipe",
                    modifier = Modifier.size(32.dp),
                    tint = BlackCharcoal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    HERBrecTheme {
        HomeScreen()
    }
}
