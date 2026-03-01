package com.example.waterreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.waterreminder.ui.FriendsScreen
import com.example.waterreminder.ui.WaterReminderScreen
import com.example.waterreminder.ui.LoginScreen
import com.example.waterreminder.ui.RegisterScreen
import com.example.waterreminder.ui.ProfileScreen
import com.example.waterreminder.ui.theme.WaterReminderTheme
import com.example.waterreminder.auth.AuthManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WaterReminderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authManager = remember { AuthManager() }
                    val navController = rememberNavController()
                    
                    // Determine start destination based on login status
                    val startDest = if (authManager.isUserLoggedIn()) "home" else "login"
                    
                    NavHost(navController = navController, startDestination = startDest) {
                        composable("home") {
                            WaterReminderScreen(
                                onNavigateToFriends = { navController.navigate("friends") },
                                onNavigateToProfile = { navController.navigate("profile") }
                            )
                        }
                        composable("friends") {
                            FriendsScreen(
                                authManager = authManager,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                authManager = authManager,
                                onNavigateBack = { navController.popBackStack() },
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                authManager = authManager,
                                onLoginSuccess = { 
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToRegister = { navController.navigate("register") }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                authManager = authManager,
                                onRegisterSuccess = { 
                                    navController.navigate("home") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}


