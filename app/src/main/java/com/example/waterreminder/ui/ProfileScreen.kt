package com.example.waterreminder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.waterreminder.auth.AuthManager
import com.example.waterreminder.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authManager: AuthManager,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val currentUserId = authManager.getCurrentUserId()
    var username by remember { mutableStateOf("Loading...") }
    var showEditDialog by remember { mutableStateOf(false) }
    var newUsername by remember { mutableStateOf("") }
    var isUpdating by remember { mutableStateOf(false) }
    
    val userEmail = authManager.getUserEmail() ?: "Not signed in"

    fun loadUserData() {
        if (currentUserId != null) {
            authManager.getUserData(currentUserId) { data, _ ->
                username = data?.get("username")?.toString() ?: "User"
                newUsername = username
            }
        }
    }

    LaunchedEffect(currentUserId) {
        loadUserData()
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        BackgroundBlobs()

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("My Profile", fontWeight = FontWeight.Bold, color = TextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(8.dp, CircleShape)
                        .background(SkyDeep, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = username.replaceFirstChar { it.uppercase() },
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextColor
                    )
                    IconButton(onClick = { showEditDialog = true }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Username", tint = SkyDeep, modifier = Modifier.size(20.dp))
                    }
                }
                Text(
                    text = userEmail,
                    fontSize = 14.sp,
                    color = TextLight
                )


                Spacer(modifier = Modifier.height(48.dp))

                // Action Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp)),
                    color = Color.White
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ProfileMenuItem(
                            icon = Icons.AutoMirrored.Filled.ExitToApp,
                            text = "Logout",
                            textColor = Color.Red,
                            onClick = {
                                authManager.signOut()
                                onLogout()
                            }
                        )
                    }
                }
            }
        }

        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Edit Username") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = newUsername,
                            onValueChange = { newUsername = it },
                            label = { Text("New Username") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newUsername.isNotEmpty()) {
                                isUpdating = true
                                authManager.updateUsername(newUsername) { success, _ ->
                                    isUpdating = false
                                    if (success) {
                                        showEditDialog = false
                                        loadUserData()
                                    }
                                }
                            }
                        },
                        enabled = !isUpdating,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SkyDeep)
                    ) {
                        if (isUpdating) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                        else Text("Save")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    textColor: Color = TextColor,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = textColor)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text, color = textColor, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }
    }
}
