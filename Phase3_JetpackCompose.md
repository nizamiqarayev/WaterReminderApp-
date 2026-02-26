# Phase 3: Jetpack Compose UI

In the past, Android developers had to write an XML file to describe the layout of a screen, and then write a separate Kotlin/Java file to make it interactive. **Jetpack Compose** changes everything: you build your UI entirely within Kotlin.

It uses a concept called "Declarative UI." You describe *what* the UI should look like for a given state, and Compose figures out *how* to draw it.

## 1. What is a `@Composable` function?

To tell the Android system that a function is meant to draw UI on the screen, you add the `@Composable` annotation above it.

*Rule:* A `@Composable` function can only be called from inside *another* `@Composable` function.

```kotlin
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// This function draws a simple line of text on the screen.
@Composable
fun GreetingMessage() {
    Text(text = "Hello, welcome to the app!")
}
```

## 2. Basic Layouts: Column, Row, Box

By default, if you put two `Text` elements in a `@Composable`, they will draw on top of each other! You need Layouts to arrange them.

1.  **`Column` (Vertical):** Stacks items top-to-bottom.
2.  **`Row` (Horizontal):** Arranges items left-to-right.
3.  **`Box` (Z-Index):** Stacks items on top of one another (like layers in Photoshop).

```kotlin
@Composable
fun UserProfile() {
    // We use a Column so the items stack vertically
    Column {
        Text(text = "Name: Alex")
        // Spacer creates empty space between elements
        Spacer(modifier = Modifier.height(16.dp)) 
        Text(text = "Age: 25")
    }
}
```

## 3. UI Components: Text, Button, Spacer

Compose gives you building blocks (Material 3 components):
*   `Text()`: Displays text. You can change font size, color, and weight.
*   `Button()`: A clickable button.
*   `Spacer()`: Empty space used to push elements apart.

```kotlin
@Composable
fun ClickableExample() {
    // The 'onClick' is a function that runs when the button is tapped.
    Button(onClick = { println("Button was clicked!") }) {
        // A Button needs to know what's inside it. In this case, Text!
        Text(text = "Click me!")
    }
}
```

## 4. `Modifier`: Changing how things look

A `Modifier` tells a UI element how to size itself, behave, or look (padding, background color, clicking, etc.). Modifiers can be chained together.

```kotlin
@Composable
fun StyledText() {
    Text(
        text = "I have padding and fill the width!",
        modifier = Modifier
            .fillMaxWidth() // Take up all available horizontal space
            .padding(16.dp) // Add 16 "density-independent pixels" of padding inside
    )
}
```

## 5. Putting it all together: The WaterReminderApp

Let's dissect the UI we built in `MainActivity.kt`:

```kotlin
@Composable
fun WaterReminderApp() {
    // 1. We create a vertical stack
    Column(
        // 2. We use Modifiers to make it fill the whole screen and add padding around the edges
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        // 3. We tell the Column to center everything horizontally and vertically
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 4. The Title
        Text(
            text = "💧 Water Reminder",
            fontSize = 32.sp, // 'sp' means scalable pixels (good for text)
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // 5. Some empty space
        Spacer(modifier = Modifier.height(32.dp)) // 'dp' means density-independent pixels
        
        Text(
            text = "Stay hydrated!",
            fontSize = 20.sp
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // 6. The Button! When clicked, it calls our sendNotification() function
        Button(onClick = { sendNotification(context) }) {
            Text("I drank water")
        }
    }
}
```

---

**Your Turn!**

Now that you understand the UI code:
1. Open `MainActivity.kt`.
2. Try changing the text `"Stay hydrated!"` to something else.
3. Try changing the spacer heights (e.g., change `48.dp` to `100.dp`).
4. Re-run your app on the emulator/device to see the changes happen!

Once you've played around with the UI, let me know, and we will move to **Phase 4: Local Functionality (Notifications)**!
