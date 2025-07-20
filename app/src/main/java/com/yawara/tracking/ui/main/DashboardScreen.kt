package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import co.yml.charts.axis.AxisData
import co.yml.charts.axis.DataCategoryOptions
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.VerticalBarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarChartType
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import com.google.common.collect.Multimaps.index
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.yawara.tracking.R
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.CheckIn
import com.yawara.tracking.domain.usecase.Utils
import com.yawara.tracking.ui.viewmodel.DashboardViewModel
import com.yawara.tracking.ui.viewmodel.PostViewModel

@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val chartDates by viewModel.chartDatesStateFlow.collectAsState()


    val user = FirebaseManager.auth.currentUser
    val userRole = viewModel.userRole.value

    // Only call once per screen lifetime
    val hasFetched = remember { mutableStateOf(false) }

    LaunchedEffect(Unit, user?.uid) {
        if (!hasFetched.value) {
            viewModel.fetchThirtyCheckInsByUser()
            hasFetched.value = true
        }

        user?.uid?.let { viewModel.fetchUserRole(it) }
    }

    Scaffold(floatingActionButton = {
        if (userRole == "admin" || userRole == "instructor") {
            FloatingActionButton(
                onClick = { navController.navigate("create_post_screen") }
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_add_circle_24),
                    contentDescription = "Crear post"
                )
            }
        }
    })
    { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Tu asistencia los últimos 30 días",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(vertical = 5.dp)
            )

            AttendanceChartCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 300.dp), chartDates
            )
            Spacer(modifier = Modifier.height(16.dp))
            RecentPosts(modifier = Modifier.fillMaxWidth(), navController)
        }
    }


}

@Composable
fun AttendanceChartCard(modifier: Modifier = Modifier, chartDates: Set<String>) {


    val maxRange = 1
    val lastThirtyDays = Utils.getLastThirtyDaysFormatted()
    val checkInDates: Set<String> = chartDates

    val barData = lastThirtyDays.mapIndexed { index, date ->
        BarData(Point(index.toFloat(), if (checkInDates.contains(date)) 1f else 0f), label = date)
    }
    val yStepSize = 1


    val xAxisData = AxisData.Builder()
        .axisStepSize(1.dp)
        .steps(barData.size - 1)
        .bottomPadding(10.dp)
        .axisLabelAngle(-35f)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .startDrawPadding(20.dp)
        .labelData { index ->
            when (index) {
                0 -> barData.first().label
                barData.lastIndex -> barData.last().label
                else -> ""
            }
        }
        .build()

    val yAxisData = AxisData.Builder()
        .steps(yStepSize)
        .labelAndAxisLinePadding(20.dp)
        .axisOffset(20.dp)
        .axisLabelColor(color = MaterialTheme.colorScheme.onBackground)
        .labelData { index -> (index * (maxRange / yStepSize)).toString() }
        .build()

    val barChartData = BarChartData(
        chartData = barData,
        horizontalExtraSpace = 8.dp,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        barStyle = BarStyle(
            paddingBetweenBars = 3.dp,
            barWidth = 8.dp
        ),
        showXAxis = true,
        backgroundColor = MaterialTheme.colorScheme.background,
        showYAxis = false
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        BarChart(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth(), barChartData = barChartData
        )
    }
}

@Composable
fun RecentPosts(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: PostViewModel = viewModel()
) {
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
                RecentPostCard(post, navController)
                Spacer(modifier = Modifier.height(16.dp))
            }
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