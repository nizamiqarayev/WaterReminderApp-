package com.example.waterreminder.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
fun FriendsScreen(
    authManager: AuthManager,
    onNavigateBack: () -> Unit
) {
    var showAddFriendDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var friendsList by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var isSearching by remember { mutableStateOf(false) }

    val currentUserId = authManager.getCurrentUserId()

    // Fetch friends list
    LaunchedEffect(currentUserId) {
        if (currentUserId != null) {
            authManager.getUserData(currentUserId) { data, _ ->
                val friendsRaw = data?.get("friends")
                val friendIds = if (friendsRaw is List<*>) {
                    friendsRaw.filterIsInstance<String>()
                } else {
                    emptyList()
                }
                
                authManager.getUsersData(friendIds) { list, _ ->
                    friendsList = list
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        BackgroundBlobs()

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Friends", fontWeight = FontWeight.Bold, color = TextColor) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (friendsList.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                            Text("No friends yet. Add some to stay hydrated together!", color = TextLight, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                        }
                    }
                }

                items(friendsList) { friendData ->
                    val name = friendData["username"]?.toString() ?: "User"
                    val intake = (friendData["cupsDrank"] as? Number)?.toInt() ?: 0
                    FriendCard(name = name, cupsDrank = intake)
                }

                item {
                    Button(
                        onClick = { showAddFriendDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SkyDeep),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Add a Friend", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (showAddFriendDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showAddFriendDialog = false
                    searchQuery = ""
                    searchResults = emptyList()
                },
                title = { Text("Search Friend") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Friend's Email") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("example@email.com") }
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = {
                                if (searchQuery.isNotEmpty()) {
                                    isSearching = true
                                    authManager.searchUser(searchQuery) { results, error ->
                                        isSearching = false
                                        searchResults = results
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isSearching,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            if (isSearching) CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                            else Text("Search")
                        }

                        if (searchResults.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Results:", fontWeight = FontWeight.Bold)
                            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(searchResults) { user ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(user["username"]?.toString() ?: "User")
                                        TextButton(onClick = {
                                            val id = user["id"]?.toString()
                                            if (id != null) {
                                                authManager.addFriend(id) { success, _ ->
                                                    if (success) showAddFriendDialog = false
                                                }
                                            }
                                        }) {
                                            Text("Add")
                                        }
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAddFriendDialog = false }) {
                        Text("Close")
                    }
                }
            )
        }
    }
}

@Composable
fun FriendCard(name: String, cupsDrank: Int, goal: Int = 8) {
    val isComplete = cupsDrank >= goal

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(20.dp))
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(if (isComplete) Green else Peach, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = if (isComplete) Color.DarkGray else TextColor)
        }

        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text(name, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextColor)
            Text("${cupsDrank} / ${goal} cups", fontSize = 14.sp, color = TextLight)
        }

        Button(
            onClick = { /* TODO: Send Firebase Notification */ },
            colors = ButtonDefaults.buttonColors(containerColor = if (isComplete) Color.LightGray else SkyDark),
            enabled = !isComplete,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isComplete) "Done" else "Remind", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

