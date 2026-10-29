package com.herbalrecipe.herbrec

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herbalrecipe.herbrec.models.Comment
import com.herbalrecipe.herbrec.ui.components.CredentialTextField
import com.herbalrecipe.herbrec.ui.theme.DarkMaroon
import com.herbalrecipe.herbrec.ui.theme.HERBrecTheme
import com.herbalrecipe.herbrec.ui.theme.RedAccent
import com.herbalrecipe.herbrec.ui.theme.RedBackground

class CommentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HERBrecTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CommentScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun CommentScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var comments by remember {
        mutableStateOf(
            listOf(
                Comment(
                    commentId = "1",
                    userName = "Username 1",
                    commentDesc = "唉！何時才會完成這些部分呢？",
                    timestamp = "3 Juli 2026, 13:25"
                ),
                Comment(
                    commentId = "2",
                    userName = "Username 2",
                    commentDesc = "몰라. 빨리 처리해야 하겠어.",
                    timestamp = "3 Juli 2026, 13:31"
                )
            )
        )
    }
    var newCommentText by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(
                        color = Color(0xFF59AC77),
                        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                // Back Button
                IconButton(
                    onClick = { (context as? Activity)?.finish() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.angle_left_24),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Title
                Text(
                    text = "Komentar (${comments.size})",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Center
                    )
                )
            }

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
                    CommentItem(
                        username = comment.userName,
                        commentDesc = comment.commentDesc,
                        timestamp = comment.timestamp
                    )
                }
            }

            // Bottom Input Bar with Text Field (Replacing Komentar & Tandai sudah dibaca buttons)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CredentialTextField(
                        value = newCommentText,
                        onValueChange = { newCommentText = it },
                        placeholderText = "Tulis komentar...",
                        modifier = Modifier.weight(1f)
                    )

                    // Send Button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(
                                color = Color(0xFF59AC77),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                if (newCommentText.isNotBlank()) {
                                    comments = comments + Comment(
                                        commentId = System.currentTimeMillis().toString(),
                                        userName = "Username 3",
                                        commentDesc = newCommentText.trim(),
                                        timestamp = "3 Juli 2026, 13:45"
                                    )
                                    newCommentText = ""
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "➤",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
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
