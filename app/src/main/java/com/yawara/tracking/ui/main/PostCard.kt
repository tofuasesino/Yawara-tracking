package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yawara.tracking.data.model.Post
import com.yawara.tracking.domain.usecase.Utils
import com.yawara.tracking.ui.navigation.Screen
import com.yawara.tracking.ui.theme.CustomTypography

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = post.title, style = CustomTypography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "${post.author}, ${Utils.parseDateWithHour(post.createdAt)}",
                    style = CustomTypography.bodyMedium,
                    //color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            /* In the future, add a overflow = overflow.ellipse (or smth similar)
            * so if the texts is quite long, the post shows three dots (. . .)
            * and by clicking we can get them to the full post in one way or another.
            * Also, for that, there should be a max size of the card. That way
            * we can achieve some visual balance!
            * */

            if (post.type == "video" && post.videoUrl.isNotEmpty()) {
                YoutubeVideoPlayer(post.videoUrl)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = post.content,
                style = CustomTypography.bodyLarge
            )
        }
    }
}


@Composable
fun RecentPostCard(post: Post, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            navController.navigate(Screen.Posts.route) {
                popUpTo(Screen.Dashboard.route) {inclusive = true}
                launchSingleTop = true
            }
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = post.title, style = CustomTypography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "${post.author}, ${Utils.parseDateWithHour(post.createdAt)}",
                    style = CustomTypography.bodyMedium,
                    //color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            /* In the future, add a overflow = overflow.ellipse (or smth similar)
            * so if the texts is quite long, the post shows three dots (. . .)
            * and by clicking we can get them to the full post in one way or another.
            * Also, for that, there should be a max size of the card. That way
            * we can achieve some visual balance!
            * */

            if (post.type == "video" && post.videoUrl.isNotEmpty()) {
                YoutubeVideoPlayer(post.videoUrl)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = post.content,
                style = CustomTypography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}