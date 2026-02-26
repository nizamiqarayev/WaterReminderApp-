# Phase 2: Android Project Anatomy

When you open an Android project, you'll see a lot of folders and files. It looks overwhelming at first, but everything has a specific purpose. Let's look at the most important parts of the Water Reminder app.

## 1. Project Structure (App vs Root)

An Android project is split into two main levels:

1.  **Root Project Level (`c:\Users\ASUS\Desktop\mobile app for fun\`):**
    This folder contains settings that apply to the *entire* codebase. Think of it as the control center for your workspace.
    *   `settings.gradle.kts`: Tells Android Studio which modules (like the `app` module) belong to this project.
    *   `build.gradle.kts`: Sets up rules and versions for tools used across all modules.

2.  **App Module Level (`c:\...\mobile app for fun\app\`):**
    This is where your actual app lives! Most of your work happens inside the `app` folder. It contains the code, images, and specific settings for the Water Reminder app itself.

## 2. Gradle Scripts (`build.gradle.kts`)

Gradle is the "builder" of your app. It takes your Kotlin code, your images, and external libraries, and squishes them together into an APK file that can be installed on a phone.

The most important file is the **App-level `build.gradle.kts`** (`app/build.gradle.kts`):

```kotlin
// Example snippets from your app/build.gradle.kts

android {
    namespace = "com.example.waterreminder" // Your app's unique ID
    compileSdk = 35                         // The newest Android version your app knows about

    defaultConfig {
        applicationId = "com.example.waterreminder" // Used by Google Play Store
        minSdk = 24                           // The oldest Android phone that can run your app
        targetSdk = 35                        // The version you specifically tested the app on
        versionCode = 1
        versionName = "1.0"                   // What the user sees (e.g., v1.0)
    }
}

dependencies {
    // This is where you bring in other people's code!
    // For example, without these lines, we couldn't build our UI with Jetpack Compose.
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.compose.material3:material3")
}
```

## 3. The `AndroidManifest.xml`

Located at `app/src/main/AndroidManifest.xml`.

Think of the Manifest as your app's ID card and instruction manual for the Android operating system. Before Android runs your app, it reads this file.

It declares:
*   **Permissions:** What your app is allowed to do.
    ```xml
    <!-- We added this so our app is allowed to send pop-up notifications! -->
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    ```
*   **The Application:** Global settings like the app's name (`@string/app_name`) and visual theme.
*   **Activities:** Every "screen" in your app must be registered here.
    ```xml
    <activity android:name=".MainActivity" ...>
        <!-- This intent-filter tells Android: "This is the very first screen to open when the user taps the app icon." -->
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    ```

## 4. The Application Lifecycle and `Activity`

An `Activity` is a single, focused thing that the user can do. Almost all activities interact with the user, so the Activity class takes care of creating a window for you to place your UI in.

In our app, `MainActivity.kt` is an Activity.

Android apps don't start with a `main()` function like standard Kotlin programs. Instead, the Android system tells your Activity when certain things happen by calling specific methods (this is the "Lifecycle").

The most important lifecycle method is `onCreate()`:

```kotlin
class MainActivity : ComponentActivity() {
    
    // onCreate is called the very first time the screen is created.
    // This is where you initialize things and tell the screen what UI to show.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // 1. We initialized our Notification Channel here
        createNotificationChannel()

        // 2. We tell the Activity to draw our Jetpack Compose UI here
        setContent {    
             WaterReminderTheme {
                 WaterReminderApp()
             }
        }
    }
}
```

Other lifecycle events exist (like `onPause()` when the user minimizes the app, or `onDestroy()` when the app is completely closed), but `onCreate()` is where almost everything starts!

---

**Your Turn!**
Now that you know what the files do, try exploring them:
1. Open up `app/build.gradle.kts`. Look at the `dependencies {}` block at the bottom. These are all the external toolkits we brought in to help us build the app.
2. Open `app/src/main/AndroidManifest.xml`. See if you can spot the `<uses-permission>` tag we added earlier.
3. Keep `MainActivity.kt` open. You'll see `override fun onCreate(...)` right at the top of the class.

Once you have reviewed this, let me know, and we will dive into **Phase 3: Jetpack Compose UI** to see exactly how we drew the "💧 Water Reminder" text and button!
