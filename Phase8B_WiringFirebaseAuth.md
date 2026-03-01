# Phase 8B: Completing the Login Screen and Wiring Firebase

You are doing great! I checked the files you created. Before we connect the button to the Firebase server, we have a few minor "typos" in your manual setup to fix so the app doesn't crash:

## 1. Quick Fixes:
1. **File Location:** You created `Auth.kt` inside the `notifications` folder, but the package inside the file says `package com.example.waterreminder.auth`. **Action:** Move the `Auth.kt` file into a new directory called `auth` (create it next to the `notifications` and `ui` folders).
2. **MainActivity Navigation:** In `MainActivity.kt`, you added `LoginScreen(onNavigateBack = { ... })`. However, our `LoginScreen.kt` expects two parameters: `onLoginSuccess` and `onNavigateToRegister`.
   **Action:** Update the `MainActivity.kt` code to correctly call it (and make sure to import it!):
   ```kotlin
   import com.example.waterreminder.ui.LoginScreen

   // Inside the NavHost:
   composable("login") {
       LoginScreen(
           onLoginSuccess = { navController.navigate("home") },
           onNavigateToRegister = { /* We will make a register screen later */ }
       )
   }
   ```
3. **Finish the UI:** In `LoginScreen.kt`, right now we only have the Email field. Add the `Password` field and a `Button` to log in!

Here is the missing piece for the password and button. Add this below the `OutlinedTextField` you already made:

```kotlin
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
            // To hide the password as they type, you normally add visualTransformation = PasswordVisualTransformation()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = {
                // WE WILL ADD FIREBASE LOGIC HERE NEXT!
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
```

## Step 2: Wiring up Firebase Auth

Once your UI is complete and `MainActivity.kt` uses the right arguments, we can connect the button to our `AuthManager`!

You will need to pass an instance of `AuthManager` into the `LoginScreen`.

Change `LoginScreen` parameter list to this:
```kotlin
fun LoginScreen(
    authManager: com.example.waterreminder.auth.AuthManager,
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
)
```

Then in `MainActivity.kt` create `val authManager = AuthManager()` and pass it into the `LoginScreen(authManager = authManager, ... )`!

---
**Action:**
Please make those quick fixes to `Auth.kt`, `MainActivity.kt`, and `LoginScreen.kt` to get the password field and button showing. Let me know when you are done!
