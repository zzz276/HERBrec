package com.herbalrecipe.herbrec.ui.components

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herbalrecipe.herbrec.R
import com.herbalrecipe.herbrec.clients.SupabaseInit
import com.herbalrecipe.herbrec.credentials.LoginActivity
import com.herbalrecipe.herbrec.ui.theme.DarkMaroon
import com.herbalrecipe.herbrec.ui.theme.RedAccent
import com.herbalrecipe.herbrec.ui.theme.RedBackground
import io.github.jan.supabase.auth.auth

@Composable
fun CommentBar(
    modifier: Modifier = Modifier,
    onCommentSubmit: (String) -> Unit = {}
) {
    val context = LocalContext.current
    var commentText by remember { mutableStateOf("") }

    val handleAuthCheck = {
        val currentSession = try {
            SupabaseInit.supabase.auth.currentSessionOrNull()
        } catch (_: Exception) {
            null
        }

        if (currentSession == null) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            true
        } else {
            false
        }
    }

    Box(
        modifier = modifier
            .padding(vertical = 20.dp)
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(52.dp)
            .background(
                color = RedBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures {
                    handleAuthCheck()
                }
            }
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = commentText,
            onValueChange = {
                if (!handleAuthCheck()) {
                    commentText = it
                }
            },
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
                if (commentText.isEmpty()) {
                    Text(
                        text = "Komentar",
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
            painter = painterResource(id = R.drawable.paper_plane_top),
            contentDescription = "Kirim Komentar",
            tint = RedAccent,
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterEnd)
                .clickable {
                    if (!handleAuthCheck()) {
                        if (commentText.isNotBlank()) {
                            onCommentSubmit(commentText)
                            commentText = ""
                        }
                    }
                }
        )
    }
}
