package com.example.waterreminder.auth

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.FieldValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthManager {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val scope = CoroutineScope(Dispatchers.Main)

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun getUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun signOut() {
        scope.launch {
            auth.signOut()
        }
    }

    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        scope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password)
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val userDoc = db.collection("users").document(uid).get()
                    if (!userDoc.exists) {
                        saveUserToFirestore(uid, email, email.split("@")[0])
                    }
                }
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun signUp(email: String, password: String, username: String, onResult: (Boolean, String?) -> Unit) {
        scope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                val uid = auth.currentUser?.uid
                if (uid != null) {
                    saveUserToFirestore(uid, email, username)
                    onResult(true, null)
                } else {
                    onResult(false, "User ID not found")
                }
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    private suspend fun saveUserToFirestore(uid: String, email: String, username: String) {
        val user = mapOf(
            "email" to email,
            "username" to username.lowercase(),
            "friends" to emptyList<String>()
        )
        db.collection("users").document(uid).set(user)
    }

    fun getUserData(uid: String, onResult: (Map<String, Any>?, String?) -> Unit) {
        scope.launch {
            try {
                val doc = db.collection("users").document(uid).get()
                onResult(doc.data(), null)
            } catch (e: Exception) {
                onResult(null, e.message)
            }
        }
    }

    fun updateUsername(newUsername: String, onResult: (Boolean, String?) -> Unit) {
        val uid = getCurrentUserId() ?: return onResult(false, "Not logged in")
        scope.launch {
            try {
                db.collection("users").document(uid).update("username" to newUsername.lowercase())
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun searchUser(emailQuery: String, onResult: (List<Map<String, Any>>, String?) -> Unit) {
        val q = emailQuery.lowercase().trim()
        scope.launch {
            try {
                val snapshot = db.collection("users")
                    .where { "email" equalTo q }
                    .get()
                val results = snapshot.documents.map { doc ->
                    val data = doc.data<Map<String, Any>>()
                    data + ("id" to doc.id)
                }
                onResult(results, null)
            } catch (e: Exception) {
                onResult(emptyList(), e.message)
            }
        }
    }

    fun addFriend(friendId: String, onResult: (Boolean, String?) -> Unit) {
        val currentUserId = getCurrentUserId() ?: return onResult(false, "Not logged in")
        if (currentUserId == friendId) return onResult(false, "You cannot add yourself")
        
        scope.launch {
            try {
                db.collection("users").document(currentUserId)
                    .update("friends" to FieldValue.arrayUnion(friendId))
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }
}
