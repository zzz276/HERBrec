package com.herbalrecipe.herbrec.ui.components

import android.app.Activity
import android.content.Intent
import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.herbalrecipe.herbrec.menus.FavoriteActivity
import com.herbalrecipe.herbrec.menus.HomeActivity
import com.herbalrecipe.herbrec.R
import com.herbalrecipe.herbrec.menus.SearchActivity
import com.herbalrecipe.herbrec.menus.UserActivity

enum class NavScreen(
    val title: String,
    @DrawableRes val iconRes: Int,
    val activityClass: Class<*>
) {
    HOME("Home", R.drawable.home_24, HomeActivity::class.java),
    SEARCH("Search", R.drawable.search_24, SearchActivity::class.java),
    FAVORITE("Favorite", R.drawable.heart_24, FavoriteActivity::class.java),
    USER("User", R.drawable.user_24, UserActivity::class.java)
}

@Composable
fun BottomNavigationBar(
    currentScreen: NavScreen,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    NavigationBar(
        modifier = modifier
    ) {
        NavScreen.entries.forEach { screen ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        val intent = Intent(context, screen.activityClass).apply {
                            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        }
                        context.startActivity(intent)
                        if (context is Activity) {
                            context.overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
                        }
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconRes),
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(text = screen.title)
                }
            )
        }
    }
}
