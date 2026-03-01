# Phase 7: Friends & Firebase Implementation Guide

Now that our local app is looking absolutely beautiful and fully functional, it is time to connect it to the internet so you and your friends can keep each other hydrated! 

## Step 1: Setting up Firebase in the Google Console

Before we write any cloud code in Kotlin, we need to rent our "server space" from Google.

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Click **Create a project** (or Add project).
3. Name it "Water Reminder" (or anything you like).
4. Google Analytics is optional, you can turn it off for now to save time.
5. Click **Create**.

## Step 2: Registering our app with Firebase

Once the project is created, Firebase needs to know exactly which app is allowed to talk to it.

1. On the Firebase dashboard overview page, click the **Android Icon** (to add an Android app).
2. For **Android package name**, you MUST enter `com.example.waterreminder` (This is the name we defined in `app/build.gradle.kts` and `MainActivity.kt`).
3. Click **Register app**.
4. Click **Download google-services.json**.
   * *Crucial Step:* Move this downloaded file into your Android app's folder exactly at: `C:\Users\ASUS\Desktop\mobile app for fun\app\google-services.json`.

## Step 3: Enabling the Services

We need to turn on the two specific services we want to use:

1. **Firestore Database:** 
   * On the left menu, click **Build > Firestore Database**.
   * Click **Create database**.
   * Start in **Test mode** (so we can easily read/write data while developing). Choose any location.
2. **Authentication:**
   * On the left menu, click **Build > Authentication**.
   * Click **Get started**.
   * Under **Sign-in method**, choose **Email/Password** and enable it.

## Step 4: Adding the Code Dependencies

Now we tell Android Studio to download the Firebase code tools. We will do this together soon, but the process involves updating our `build.gradle.kts` files to include lines like:
`implementation("com.google.firebase:firebase-firestore")`

## Step 5: The "Friends" Architecture

To support friends, our app needs to go from having one screen to multiple screens (Navigation).

1. **WaterReminderScreen (Home):** Where you track your own water.
2. **FriendsScreen:** A list of your friends and their progress.

We will use Jetpack Compose Navigation (`androidx.navigation.compose`) to switch between these two views smoothly. When you click a "Remind" button next to a friend's name, we will write a document into the Firestore Database that triggers a notification on their phone!

---
**Your Action Plan:** 
I am currently writing the code for the `FriendsScreen.kt` UI and setting up the Navigation so our app has multiple pages. 

While I do that, please complete **Steps 1, 2, and 3** above! Let me know if you run into any issues creating the Firebase project.
