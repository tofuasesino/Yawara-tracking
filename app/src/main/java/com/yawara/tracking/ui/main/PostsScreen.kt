package com.yawara.tracking.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yawara.tracking.viewmodel.PostViewModel


@Composable
fun PostsScreen(viewModel: PostViewModel = viewModel()) {

    val postList = viewModel.posts.value

    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(postList.size) {
            PostCard(postList[it])
        }
    }
}