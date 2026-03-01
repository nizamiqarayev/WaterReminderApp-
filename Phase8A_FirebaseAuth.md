# Phase 8A: Firebase Authentication

Since you want to write the code yourself, I will guide you step-by-step! We will start with Authentication so users can create accounts and log in.

## Step 1: Add the Auth Dependency

First, we need the specific Firebase code for Authentication.

1. Open `app/build.gradle.kts`.
2. Find your `dependencies { ... }` block at the bottom.
3. Add this line right below your other Firebase dependencies:
   ```kotlin
   implementation("com.google.firebase:firebase-auth")
   ```
4. Click **Sync Now** at the top right of Android Studio.

## Step 2: Create an Auth Manager

Just like we created `WaterNotificationManager` to handle notifications cleanly, we should create a helper for Authentication.

1. In your project tree (`app/src/main/java/com/example/waterreminder`), right-click on the `notifications` package folder and create a NEW package called `auth`.
2. Inside `auth`, create a new Kotlin class/file called `AuthManager.kt`.
3. Copy the following structure and fill in the logic:

```kotlin
package com.example.waterreminder.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthManager {
    // This gets the instance of Firebase Auth connected to your project
    private val auth: FirebaseAuth = Firebase.auth

    // Helper to check if someone is already logged in
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // You will use these methods later!
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
```

## Step 3: Create the Login UI

We need a screen where users can type their email and password.

1. Go to your `ui` package and create a new Kotlin file called `LoginScreen.kt`.
2. Write a Composable function that takes two callbacks: `onLoginSuccess` and `onNavigateToRegister`.
3. You will need `TextField` for email and password input, and a `Button` to submit.

Here is a skeleton to start with:

```kotlin
package com.example.waterreminder.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit // If they need to create an account
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Login to Water Reminder", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // ... Add Password field here ...
        
        // ... Add Login button here ...
    }
}
```

---
**Your Turn!**
1. Add the dependency.
2. Create `AuthManager.kt`.
3. Create `LoginScreen.kt` and try to finish the Password field and Login button! 

When you have these three files set up, let me know, and I will show you how to connect the Login button to Firebase to actually verify the password!
