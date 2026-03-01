package com.example.waterreminder.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.waterreminder.notifications.WaterNotificationManager
import com.example.waterreminder.auth.AuthManager
import com.example.waterreminder.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WaterReminderScreen(
    authManager: AuthManager = remember { AuthManager() },
    onNavigateToFriends: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val context = LocalContext.current
    val notificationManager = remember { WaterNotificationManager(context) }
    
    var cupsDrank by remember { mutableStateOf(0) }
    val goal = 8
    var showCelebration by remember { mutableStateOf(false) }

    val currentUserId = authManager.getCurrentUserId()

    // Load initial data
    LaunchedEffect(currentUserId) {
        if (currentUserId != null) {
            authManager.getUserData(currentUserId) { data, _ ->
                val savedCups = (data?.get("cupsDrank") as? Number)?.toInt() ?: 0
                cupsDrank = savedCups
            }
        }
    }

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheckResult = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // Floating Background Blobs
        BackgroundBlobs()

        // Main Content Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            HeaderSection(onNavigateToFriends, onNavigateToProfile)
            
            Box(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CupSection(cupsDrank = cupsDrank, goal = goal)
                    
                    MessageSection(cupsDrank = cupsDrank)
                    
                    DrinkButton(cupsDrank = cupsDrank, goal = goal) {
                        if (cupsDrank < goal) {
                            val nextCup = cupsDrank + 1
                            cupsDrank = nextCup
                            authManager.updateWaterIntake(nextCup)
                            notificationManager.sendWaterDrankNotification()
                            if (nextCup == goal) {
                                showCelebration = true
                            }
                        }
                    }
                    
                    CupsGridSection(cupsDrank = cupsDrank, goal = goal)
                    
                    ReminderSection()
                    
                    ResetButton { 
                        cupsDrank = 0
                        showCelebration = false 
                        authManager.updateWaterIntake(0)
                    }
                }
            }
        }

        if (showCelebration) {
            CelebrationOverlay { showCelebration = false }
        }
    }
}

@Composable
fun BackgroundBlobs() {
    // A simplified visual representation of the HTML background blobs
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .size(420.dp)
                .offset(x = 100.dp, y = (-80).dp)
                .align(Alignment.TopEnd)
                .alpha(0.45f)
                .background(Sky, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = (-60).dp, y = 60.dp)
                .align(Alignment.BottomStart)
                .alpha(0.45f)
                .background(Peach, CircleShape)
        )
    }
}

@Composable
fun HeaderSection(onNavigateToFriends: () -> Unit, onNavigateToProfile: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFFE8F6FD), Color(0xFFD4EEF9))))
            .padding(top = 36.dp, start = 28.dp, end = 28.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Good morning, ☀️", color = SkyDeep, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text("Stay hydrated today!", color = TextColor, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onNavigateToFriends,
                modifier = Modifier.background(Color.White.copy(alpha=0.5f), CircleShape)
            ) {
                Icon(Icons.Default.Person, contentDescription = "Friends", tint = SkyDeep)
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            IconButton(
                onClick = onNavigateToProfile,
                modifier = Modifier.background(Color.White.copy(alpha=0.5f), CircleShape)
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = SkyDeep)
            }
        }
    }
}

@Composable
fun CupSection(cupsDrank: Int, goal: Int) {
    val mascots = listOf("🐸", "🦋", "🌸", "🐬", "✨", "🦄", "🌊", "🎉")
    val currentMascot = mascots[minOf(cupsDrank, mascots.lastIndex)]
    
    val progressPct = cupsDrank.toFloat() / goal

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Mascot
        Text(currentMascot, fontSize = 36.sp, modifier = Modifier.padding(bottom = 8.dp))
        
        // Progress Bar Mock
        val animatedWidth by animateFloatAsState(
            targetValue = progressPct, 
            animationSpec = tween(800, easing = LinearOutSlowInEasing),
            label = "ProgressBar"
        )
        
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$cupsDrank", fontSize = 42.sp, fontWeight = FontWeight.Bold, color = SkyDeep)
            Text(" / $goal cups", fontSize = 16.sp, color = TextLight, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 6.dp, start = 4.dp))
        }
        Text("daily hydration goal 💧", fontSize = 13.sp, color = TextLight, modifier = Modifier.padding(top = 4.dp))
        
        // Progress Bar
        Box(modifier = Modifier.fillMaxWidth().padding(top = 18.dp).height(14.dp).background(Peach, CircleShape)) {
             Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(animatedWidth).background(Brush.horizontalGradient(listOf(SkyDark, SkyDeep)), CircleShape))
        }
    }
}

@Composable
fun MessageSection(cupsDrank: Int) {
    val messages = listOf(
        "Drink your first cup of water today! You can do it! 🌸",
        "Great start! Keep it up, you're doing amazing! 🌸",
        "Sip sip hooray! Two cups down! 🎉",
        "Halfway there! Your skin is glowing already ✨",
        "You're doing so well! Just a few more cups 💪",
        "Almost there! One more push! 🌊",
        "Look at you go! So close to your goal! 🌟",
        "One more and you're done! You've got this! 🍀",
        "WOW! You reached your goal today! Amazing! 🎊"
    )
    val msg = messages[minOf(cupsDrank, messages.lastIndex)]
    
    Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 28.dp, vertical = 10.dp).background(Peach, RoundedCornerShape(20.dp)).padding(12.dp)) {
        Text("💬 $msg", fontSize = 13.sp, color = TextColor, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun DrinkButton(cupsDrank: Int, goal: Int, onClick: () -> Unit) {
    val isEnabled = cupsDrank < goal
    Button(
        onClick = onClick,
        enabled = isEnabled,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .height(56.dp)
            .shadow(if (isEnabled) 12.dp else 0.dp, CircleShape, spotColor = SkyDeep)
            .background(Brush.linearGradient(listOf(SkyDark, SkyDeep)), CircleShape)
            .alpha(if (isEnabled) 1f else 0.5f)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            Text("💧", fontSize = 20.sp, modifier = Modifier.padding(end = 10.dp))
            Text("I drank a cup!", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun CupsGridSection(cupsDrank: Int, goal: Int) {
    Column(modifier = Modifier.padding(top = 20.dp, start = 28.dp, end = 28.dp)) {
        Text("TODAY'S CUPS", fontSize = 12.sp, color = TextLight, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            for (i in 0 until goal) {
                val isFilled = i < cupsDrank
                Box(
                    modifier = Modifier.size(40.dp).background(if (isFilled) Sky else Peach.copy(alpha=0.5f), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(if (isFilled) "🥛" else "🫙", fontSize = 22.sp)
                }
            }
        }
    }
}

@Composable
fun ReminderSection() {
    var reminderOn by remember { mutableStateOf(true) }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 20.dp, start = 28.dp, end = 28.dp).fillMaxWidth().background(Brush.linearGradient(listOf(Color(0xFFF0FAF0), Color(0xFFDDF4DD))), RoundedCornerShape(20.dp)).padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        Text("⏰", fontSize = 28.sp)
        Column(modifier = Modifier.weight(1f).padding(horizontal = 14.dp)) {
            Text("Hourly reminders", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextColor)
            Text("Gentle nudges every hour", fontSize = 11.sp, color = TextLight)
        }
        Switch(
            checked = reminderOn,
            onCheckedChange = { reminderOn = it },
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = SkyDark, uncheckedThumbColor = Color.White, uncheckedTrackColor = Color.LightGray)
        )
    }
}

@Composable
fun ResetButton(onClick: () -> Unit) {
    Text(
        text = "↩ Reset today",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = TextLight,
        modifier = Modifier.fillMaxWidth().padding(top = 18.dp).clickable(onClick = onClick),
        textAlign = TextAlign.Center
    )
}

@Composable
fun CelebrationOverlay(onClose: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Sky.copy(alpha = 0.85f)).clickable(onClick = onClose),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉", fontSize = 80.sp)
            Text("You did it, superstar! 🌟", fontSize = 26.sp, color = TextColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 16.dp))
            Text("You've drunk all 8 cups today!\nYour body thanks you! 💙", color = TextColor, textAlign = TextAlign.Center, fontSize = 14.sp)
            Button(onClick = onClose, colors = ButtonDefaults.buttonColors(containerColor = SkyDeep), modifier = Modifier.padding(top = 24.dp)) {
                Text("Yay! 🎊", fontWeight = FontWeight.Bold)
            }
        }
    }
}

