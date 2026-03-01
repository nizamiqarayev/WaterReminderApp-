package com.example.waterreminder.auth

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.FieldValue

class AuthManager {
    private val TAG = "AuthManager"
    private val auth: FirebaseAuth = Firebase.auth
    private val db = Firebase.firestore

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        // Check if document exists, if not create basic one
                        db.collection("users").document(uid).get().addOnSuccessListener { doc ->
                            if (!doc.exists()) {
                                saveUserToFirestore(uid, email, email.split("@")[0], onResult)
                            } else {
                                onResult(true, null)
                            }
                        }
                    } else {
                        onResult(true, null)
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    fun signUp(email: String, password: String, username: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid
                    if (uid != null) {
                        saveUserToFirestore(uid, email, username, onResult)
                    } else {
                        onResult(false, "User ID not found")
                    }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    private fun saveUserToFirestore(uid: String, email: String, username: String, onResult: (Boolean, String?) -> Unit) {
        val user = hashMapOf(
            "email" to email,
            "username" to username.lowercase(),
            "friends" to emptyList<String>() // Initialize empty friends list
        )
        Log.d(TAG, "Attempting to save user to Firestore: $uid")
        db.collection("users").document(uid).set(user)
            .addOnSuccessListener { 
                Log.d(TAG, "Successfully saved user to Firestore")
                onResult(true, null) 
            }
            .addOnFailureListener { 
                Log.e(TAG, "Failed to save user to Firestore: ${it.message}")
                onResult(false, it.message) 
            }
    }

    fun getUserData(uid: String, onResult: (Map<String, Any>?, String?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { onResult(it.data, null) }
            .addOnFailureListener { onResult(null, it.message) }
    }

    fun getUserEmail(): String? {
        return auth.currentUser?.email
    }

    fun signOut() {
        auth.signOut()
    }

    // Update User Data
    fun updateUsername(newUsername: String, onResult: (Boolean, String?) -> Unit) {
        val uid = getCurrentUserId() ?: return onResult(false, "Not logged in")
        val lowercaseUsername = newUsername.lowercase()
        
        db.collection("users").document(uid)
            .update("username", lowercaseUsername)
            .addOnSuccessListener { 
                Log.d(TAG, "Username updated successfully in Firestore")
                onResult(true, null) 
            }
            .addOnFailureListener { 
                Log.e(TAG, "Failed to update username: ${it.message}")
                onResult(false, it.message) 
            }
    }

    // Social Features
    fun searchUser(emailQuery: String, onResult: (List<Map<String, Any>>, String?) -> Unit) {
        val q = emailQuery.lowercase().trim()
        
        // Search ONLY by exact email
        db.collection("users")
            .whereEqualTo("email", q)
            .get()
            .addOnSuccessListener { docs ->
                val results = docs.map { it.data + ("id" to it.id) }
                onResult(results, null)
            }
            .addOnFailureListener { onResult(emptyList(), it.message) }
    }

    fun addFriend(friendId: String, onResult: (Boolean, String?) -> Unit) {
        val currentUserId = getCurrentUserId() ?: return onResult(false, "Not logged in")
        if (currentUserId == friendId) return onResult(false, "You cannot add yourself")

        // In a real app, you'd use a subcollection "friends" or a separate collection
        // For simplicity: add to an array in the user's document
        db.collection("users").document(currentUserId)
            .update("friends", FieldValue.arrayUnion(friendId))
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { onResult(false, it.message) }
    }
}