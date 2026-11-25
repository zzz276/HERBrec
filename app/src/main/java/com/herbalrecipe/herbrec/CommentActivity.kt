package com.herbalrecipe.herbrec

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herbalrecipe.herbrec.models.AppDatabase
import com.herbalrecipe.herbrec.models.Comment
import com.herbalrecipe.herbrec.ui.components.AppBar
import com.herbalrecipe.herbrec.ui.components.CommentBar
import com.herbalrecipe.herbrec.ui.components.formatDateTimeString
import com.herbalrecipe.herbrec.ui.theme.DarkMaroon
import com.herbalrecipe.herbrec.ui.theme.HERBrecTheme
import com.herbalrecipe.herbrec.ui.theme.RedAccent
import com.herbalrecipe.herbrec.ui.theme.RedBackground

class CommentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val recipeId = intent.getStringExtra("EXTRA_RECIPE_ID") ?: ""
        setContent {
            HERBrecTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CommentScreen(recipeId = recipeId, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CommentScreen(
    modifier: Modifier = Modifier,
    recipeId: String = ""
) {
    val context = LocalContext.current
    var comments by remember { mutableStateOf<List<Comment>>(emptyList()) }
    val userNamesMap = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(recipeId) {
        val db = AppDatabase.getInstance(context.applicationContext)
        try {
            val fetched = if (recipeId.isNotEmpty()) {
                db.commentDao().getCommentsForRecipe(recipeId)
            } else {
                db.commentDao().getAllComments()
            }

            if (fetched.isNotEmpty()) { comments = fetched }
        } catch (e: Exception) { e.printStackTrace() }
    }

    LaunchedEffect(comments.size) {
        if (comments.isNotEmpty()) {
            val db = AppDatabase.getInstance(context.applicationContext)
            comments.forEach { comment ->
                if (comment.userId.isNotBlank() && !userNamesMap.containsKey(comment.userId)) {
                    val user = db.userDao().getUserById(comment.userId)
                    if (user != null && user.userName.isNotBlank()) {
                        userNamesMap[comment.userId] = user.userName
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Top Bar
            AppBar(context, "Komentar (${comments.size})")
            Spacer(modifier = Modifier.height(16.dp))

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(comments) { comment ->
                    val authorName = userNamesMap[comment.userId] ?: if (comment.userName.isNotBlank()) comment.userName else if (comment.userId.isNotBlank()) comment.userId else "Pengguna"
                    val formattedTime = formatDateTimeString(comment.createdAt)
                    CommentItem(
                        username = authorName,
                        commentDesc = comment.commentDesc,
                        timestamp = formattedTime
                    )
                }
            }

            CommentBar()
        }
    }
}

@Composable
fun CommentItem(
    username: String,
    commentDesc: String,
    timestamp: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        // Comment Bubble Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .background(
                    color = RedBackground,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(start = 20.dp, end = 20.dp, top = 28.dp, bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = commentDesc,
                    style = TextStyle(
                        color = Color(0xFF1A1C19),
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = timestamp,
                    modifier = Modifier.align(Alignment.End),
                    style = TextStyle(
                        color = Color(0xFF1A1C19),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        }

        // Username Pill Badge
        Box(
            modifier = Modifier
                .padding(start = 24.dp)
                .background(
                    color = RedAccent,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = username,
                style = TextStyle(
                    color = DarkMaroon,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Serif
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentScreenPreview() {
    HERBrecTheme {
        CommentScreen()
    }
}
