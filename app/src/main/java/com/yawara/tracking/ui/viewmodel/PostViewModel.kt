package com.yawara.tracking.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.Post
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val db = FirebaseManager.firestore
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts

    private val _recentPosts = mutableStateOf<List<Post>>(emptyList())
    val recentPosts: State<List<Post>> = _recentPosts

    fun loadRecentPosts() {
        viewModelScope.launch {
            _recentPosts.value = FirebaseManager.fetchRecentPosts()

        }
    }
}