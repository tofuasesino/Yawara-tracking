package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.viewinterop.AndroidView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.yawara.tracking.domain.model.Post
import com.yawara.tracking.domain.usecase.Utils
import com.yawara.tracking.ui.viewmodel.DashboardViewModel
import com.yawara.tracking.ui.viewmodel.PostViewModel

@Composable
fun DashboardScreen(
    dashBoardViewModel: DashboardViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .verticalScroll(rememberScrollState())
    ) {


        Text(text = "Dashboard", style = MaterialTheme.typography.displayLarge)

        AttendanceChartCard(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecentPosts(modifier = Modifier.fillMaxWidth())

    }


}

@Composable
fun AttendanceChartCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Text(
            text = "Aquí irá el gráfico",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(5.dp)
        )

    }
}

@Composable
fun RecentPosts(modifier: Modifier = Modifier, viewModel: PostViewModel = viewModel()) {
    val recentPosts by viewModel.recentPosts

    LaunchedEffect(Unit) {
        viewModel.loadRecentPosts()
    }

    Column(modifier = modifier) {
        Text(
            text = "Noticias recientes",
            modifier = Modifier.padding(vertical = 8.dp),
            style = MaterialTheme.typography.displayLarge
        )
        if (recentPosts.isEmpty()) {
            Text(
                "No recent posts found.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            recentPosts.forEach { post ->
                PostCard(post)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(text = post.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = "${post.author}, ${Utils.parseDate(post.createdAt)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun YoutubeVideoPlayer(videoUrl: String) {
    val videoId = Utils.extractVideoId(videoUrl)

    AndroidView(
        factory = { context ->
            val youtubePlayerView = YouTubePlayerView(context).apply {
                addYouTubePlayerListener(object : YouTubePlayerListener {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        videoId?.let { youTubePlayer.cueVideo(it, 0f) }
                    }

                    override fun onStateChange(
                        youTubePlayer: YouTubePlayer,
                        state: PlayerConstants.PlayerState
                    ) {
                    }


                    override fun onPlaybackQualityChange(
                        youTubePlayer: YouTubePlayer,
                        playbackQuality: PlayerConstants.PlaybackQuality
                    ) {
                    }

                    override fun onPlaybackRateChange(
                        youTubePlayer: YouTubePlayer,
                        playbackRate: PlayerConstants.PlaybackRate
                    ) {
                    }

                    override fun onError(
                        youTubePlayer: YouTubePlayer,
                        error: PlayerConstants.PlayerError
                    ) {
                    }

                    override fun onApiChange(youTubePlayer: YouTubePlayer) {
                    }

                    override fun onCurrentSecond(
                        youTubePlayer: YouTubePlayer,
                        second: Float
                    ) {
                    }

                    override fun onVideoDuration(
                        youTubePlayer: YouTubePlayer,
                        duration: Float
                    ) {
                    }

                    override fun onVideoId(
                        youTubePlayer: YouTubePlayer,
                        videoId: String
                    ) {

                    }

                    override fun onVideoLoadedFraction(
                        youTubePlayer: YouTubePlayer,
                        loadedFraction: Float
                    ) {
                    }
                })
                enableAutomaticInitialization = false
            }
            youtubePlayerView

        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Adjust the height based on your preference
    )
}