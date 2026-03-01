# Phase 8C: Manual Login & Register Implementation

Now that we have the UI and the build fixed, let's write the actual logic that talks to the Firebase servers!

## Step 1: Add methods to `AuthManager.kt`

Open `AuthManager.kt` and add these two methods inside the class. These use the Firebase SDK to verify credentials.

```kotlin
    // Sign in an existing user
    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Register a new user
    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
```

## Step 2: Wire up `LoginScreen.kt`

Now, let's make the Login button actually do something. Update the `onClick` of your button in `LoginScreen.kt`:

```kotlin
      
```

## Step 3: Create `RegisterScreen.kt`

This looks almost exactly like the Login screen! Create `RegisterScreen.kt` in your `ui` folder.

**Skeleton for RegisterScreen:**
```kotlin
@Composable
fun RegisterScreen(
    authManager: AuthManager,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // 1. Create email and password states
    // 2. Create the UI (Text, OutlinedTextFields, Buttons)
    // 3. In the Button onClick, call authManager.signUp(...)
}
```

## Step 4: Update `MainActivity.kt`

Finally, we need to tell the app that the "register" screen exists.

```kotlin
    composable("register") {
        RegisterScreen(
            authManager = authManager,
            onRegisterSuccess = { navController.navigate("home") },
            onNavigateToLogin = { navController.popBackStack() }
        )
    }
```

---
**Your Task:**
1. Update `AuthManager.kt` with the new methods.
2. Update the button in `LoginScreen.kt`.
3. Create `RegisterScreen.kt` (You can copy-paste most of LoginScreen for this!).
4. Add the route to `MainActivity.kt`.

When you are ready, I can help you add "Error Messages" so the user knows if they typed the wrong password!
