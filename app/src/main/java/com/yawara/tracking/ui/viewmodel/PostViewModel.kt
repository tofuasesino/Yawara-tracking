package com.yawara.tracking.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Query
import com.yawara.tracking.data.datasource.FirebaseManager
import com.yawara.tracking.domain.model.Post
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostViewModel : ViewModel() {

    private val db = FirebaseManager.firestore.collection("posts")
    private val _posts = mutableStateOf<List<Post>>(emptyList())
    val posts: State<List<Post>> get() = _posts

    private val _recentPosts = mutableStateOf<List<Post>>(emptyList())
    val recentPosts: State<List<Post>> = _recentPosts

    fun loadRecentPosts() {
        viewModelScope.launch {
            _recentPosts.value = fetchRecentPosts()

        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            _posts.value = fetchPosts()
        }
    }

    suspend fun fetchPosts(): List<Post> {
        return try {
            val snapshot = db
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun fetchRecentPosts(): List<Post> {
        return try {
            val snapshot = db
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(2)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }


}