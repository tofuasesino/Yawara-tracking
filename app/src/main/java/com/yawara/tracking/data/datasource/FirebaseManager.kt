package com.yawara.tracking.data.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.yawara.tracking.domain.model.Post
import kotlinx.coroutines.tasks.await

object FirebaseManager {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val firestore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }


    suspend fun fetchPosts(): List<Post> {
        return try {
            val snapshot = firestore
                .collection("posts")
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
            val snapshot = firestore
                .collection("posts")
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