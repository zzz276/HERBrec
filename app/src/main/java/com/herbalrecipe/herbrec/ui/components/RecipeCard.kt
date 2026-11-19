package com.herbalrecipe.herbrec.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.herbalrecipe.herbrec.R
import com.herbalrecipe.herbrec.ui.theme.RedBackground

@Composable
fun RecipeCard(
    recipeTitle: String,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCommentClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = RedBackground,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Row Icons (Heart & Comment with Badge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Heart Icon
                Icon(
                    painter = painterResource(id = R.drawable.heart_24),
                    contentDescription = "Favorite",
                    tint = Color(0xFF1A1C19),
                    modifier = Modifier.size(32.dp)
                )

                // Comment Icon with Badge
                Box(
                    modifier = Modifier.clickable { onCommentClick() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.comment_alt_24),
                        contentDescription = "Comment",
                        tint = Color(0xFF1A1C19),
                        modifier = Modifier.size(32.dp)
                    )
                    // Red Notification Badge Dot
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(Color(0xFFFF5B5B), shape = CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // White Image Container Placeholder
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Recipe Title Text
            Text(
                text = recipeTitle,
                style = TextStyle(
                    color = Color(0xFF1A1C19),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Detail Button
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(46.dp)
                    .clip(RoundedCornerShape(23.dp))
                    .background(Color(0xFF59AC77))
                    .clickable { onDetailClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Detail",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Serif
                    )
                )
            }
        }
    }
}
