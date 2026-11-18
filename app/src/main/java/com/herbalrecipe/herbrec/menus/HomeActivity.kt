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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.herbalrecipe.herbrec.CreateRecipeActivity
import com.herbalrecipe.herbrec.ViewRecipeActivity
import com.herbalrecipe.herbrec.clients.SupabaseInit
import com.herbalrecipe.herbrec.clients.fetchAllRecipes
import com.herbalrecipe.herbrec.models.Recipe
import com.herbalrecipe.herbrec.ui.components.BottomNavigationBar
import com.herbalrecipe.herbrec.ui.components.NavScreen
import com.herbalrecipe.herbrec.ui.components.RecipeCard
import com.herbalrecipe.herbrec.ui.theme.GreenSupport
import com.herbalrecipe.herbrec.ui.theme.HERBrecTheme
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashscreen = installSplashScreen()
        var keepSplashScreen = true
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashscreen.setKeepOnScreenCondition { keepSplashScreen }
        lifecycleScope.launch {
            delay(5000.milliseconds)
            keepSplashScreen = false
        }

        setContent {
            HERBrecTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val recipes = remember { mutableStateListOf<Recipe>() }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading = true
        try {
            val fetched = fetchAllRecipes()
            if (fetched.isNotEmpty()) {
                recipes.clear()
                recipes.addAll(fetched)
            } else {
                recipes.add(Recipe(recipeId = "2", recipeTitle = "Resep 2"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            recipes.add(Recipe(recipeId = "2", recipeTitle = "Resep 2"))
        } finally { isLoading = false }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavigationBar(currentScreen = NavScreen.HOME) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(GreenSupport),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HERBrec",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontStyle = FontStyle.Italic,
                            fontFamily = FontFamily.Serif,
                            textAlign = TextAlign.Center,
                        )
                    )
                }

                // Recipe Cards List
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF59AC77))
                        }
                    } else {
                        recipes.forEach { recipe ->
                            RecipeCard(
                                recipeTitle = recipe.recipeTitle,
                                onDetailClick = {
                                    val intent = Intent(context, ViewRecipeActivity::class.java).apply {
                                        putExtra("EXTRA_RECIPE_TITLE", recipe.recipeTitle)
                                        putExtra("EXTRA_RECIPE_INGREDIENTS", recipe.recipeIngredients)
                                        putExtra("EXTRA_RECIPE_STEPS", recipe.recipeSteps)
                                        putExtra("EXTRA_RECIPE_SERVES", recipe.recipeServes)
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
                        border = BorderStroke(1.5.dp, Color.Black),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable {
                        val intent = Intent(context, CreateRecipeActivity::class.java)
                        context.startActivity(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    )
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
