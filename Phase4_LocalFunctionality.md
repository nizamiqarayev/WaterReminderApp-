# Phase 4: Local Functionality

So far, we've built the screen. But how does the app actually *do* something on the phone, like sending a notification? To interact with the Android operating system, we need to understand Context, Permissions, and the Notification Manager.

## 1. What is `Context`?

You will see the word `Context` everywhere in Android development. 

Think of `Context` as your app's **handle** to the Android system. It allows you to:
- Access resources (like strings or images)
- Open a new screen (Activity)
- Get access to system services (like alarms, location, or **notifications**)

In `WaterReminderScreen.kt`, we get the Context like this:
```kotlin
val context = LocalContext.current
```
We then pass this `context` into our `WaterNotificationManager` so it has the "authority" to talk to the Android system.

## 2. Permissions (Android 13+)

Historically, apps could just send notifications whenever they wanted. Starting with Android 13 (Tiramisu), apps must explicitly ask the user for permission first.

This involves two steps:

**Step A: Declare it in the rulebook (`AndroidManifest.xml`)**
```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

**Step B: Ask the user at runtime (`WaterReminderScreen.kt`)**
In Compose, asking for permission looks a bit tricky, but here is the flow:
```kotlin
// 1. Create a "Launcher". This is what will actually show the popup to the user.
val notificationPermissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
) { isGranted: Boolean ->
    // We could do something here if they say Yes or No
}

// 2. Automatically run this block of code when the screen opens
LaunchedEffect(Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // 3. Check if we ALREADY have permission
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        )
        
        // 4. If we don't have it, launch the popup asking for it!
        if (permissionCheckResult != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
```

## 3. Local Notifications

Now that we have permission, how do we send the notification? Let's look inside `WaterNotificationManager.kt`.

### Step A: Create a Notification Channel

Since Android 8 (Oreo), every notification must belong to a "Channel". If an app has a "Marketing" channel and a "Reminders" channel, the user can turn off the marketing ones without breaking the reminders.

```kotlin
private fun createNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "WATER_REMINDER", // ID
            "Water Reminder Channel", // Name user sees in settings
            NotificationManager.IMPORTANCE_DEFAULT // How aggressively it interrupts the user
        )
        // Ask the system to create it
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
```

### Step B: Build and Send the Notification

When the user clicks "I drank water", we do two things: build the message, and tell the system to show it.

```kotlin
fun sendWaterDrankNotification() {
    // 1. BUILD the notification using the same Channel ID from above
    val builder = NotificationCompat.Builder(context, "WATER_REMINDER")
        .setSmallIcon(android.R.drawable.ic_dialog_info) // The little icon at the top of the screen
        .setContentTitle("Great job!")
        .setContentText("You successfully drank water. Keep it up!")

    // 2. SEND the notification
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    
    // We give it an ID (1001). If we send another notification with ID 1001, 
    // it will OVERWRITE the old one rather than spamming the user's screen.
    notificationManager.notify(1001, builder.build()) 
}
```

---

**Your Turn!**

Let's modify the notification:
1. Open `app/src/main/java/com/example/waterreminder/notifications/WaterNotificationManager.kt`.
2. Find the `sendWaterDrankNotification()` method.
3. Change the `.setContentTitle(...)` to something like `"Hydration Goal"`
4. Change the `.setContentText(...)` to `"Glug glug glug!"`
5. Re-run your app, click the button, and see your custom notification!

Let me know when you are done messing with the notifications, and we will do a brief overview of **Phase 5: Online Features (Firebase)** so you understand what it takes to add friends!
