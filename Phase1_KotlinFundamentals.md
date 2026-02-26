# Phase 1: Kotlin Fundamentals

Welcome to Kotlin! Kotlin is a modern, concise, and safe programming language. It is the official language for Android development. Let's break down the fundamentals you need to understand the Water Reminder app.

## 1. Variables (`val` vs `var`) and Basic Types

In Kotlin, you store data in variables. You must decide whether a variable can be changed later or if it's constant.

- **`val` (Value):** Immutable. Once you assign a value to a `val`, you **cannot** change it. It's like a constant. Use this by default!
- **`var` (Variable):** Mutable. You can change its value later.

```kotlin
// Examples:
val appName = "Water Reminder" // Cannot be changed later
// appName = "New Name" // ERROR!

var glassesDrank = 0 // Can be changed
glassesDrank = 1     // Perfectly fine!

// Kotlin is smart (Type Inference). It knows `glassesDrank` is an Integer (Int) 
// because you assigned 0 to it. You don't have to explicitly say it's an Int.
// But you can if you want to:
val goal: Int = 8
val isHydrated: Boolean = false
val dailyMessage: String = "Keep drinking!"
```

## 2. Functions (Syntax, Parameters, Return Types)

Functions are reusable blocks of code that perform a specific task. They are declared using the `fun` keyword.

```kotlin
// 1. A basic function that takes no input and returns nothing (Unit)
fun sayHello() {
    println("Hello, Android Developer!")
}

// 2. A function that takes input (parameters)
// It takes a String named `personName`
fun greet(personName: String) {
    println("Hello, $personName!") // The $ sign lets you put variables inside strings
}

// 3. A function that takes input AND returns a result
// The `: Int` at the end means this function will spit out an Integer.
fun addNumbers(a: Int, b: Int): Int {
    val sum = a + b
    return sum
}

// 4. Shorthand (Single-Expression Function)
fun multiply(a: Int, b: Int): Int = a * b
```
*In our app, look at `fun sendNotification(context: Context)` in MainActivity.kt. It's a function that takes a `Context` as input and returns nothing.*

## 3. Control Flow (`if`, `when`, `for`, `while`)

How does your app make decisions?

### `if / else`
Standard decision making.
```kotlin
val glasses = 3
if (glasses >= 8) {
    println("Goal reached!")
} else {
    println("Keep drinking!")
}
```

### `when`
Kotlin's superpower version of `switch` statements. It's cleaner and more powerful.
```kotlin
val timeOfDay = "Morning"

when (timeOfDay) {
    "Morning" -> println("Drink a glass of water to wake up!")
    "Afternoon" -> println("Stay hydrated during work.")
    "Night" -> println("Don't drink too much or you'll wake up!")
    else -> println("Just keep drinking.") // Default case
}
```

## 4. Classes and Object-Oriented Programming (OOP)

Kotlin is Object-Oriented. You create blueprints (`class`) and then build real objects from them.

```kotlin
// Creating a blueprint for a User
class User(val name: String, var age: Int) {
    
    // Functions inside a class are called "methods"
    fun celebrateBirthday() {
        age = age + 1
        println("Happy birthday $name, you are now $age!")
    }
}

// Creating an actual User object
fun main() {
    val myUser = User(name = "Alex", age = 25)
    println(myUser.name) // Prints "Alex"
    myUser.celebrateBirthday() // Increments age to 26
}
```
*In our app, `class MainActivity : ComponentActivity()` is a class. The `:` means it **inherits** from `ComponentActivity`, getting all the powers of an Android screen automatically!*

## 5. Null Safety (`?`, `!!`, `?.`, `?:`) - Kotlin's Best Feature

In many languages (like Java), an empty variable (`null`) crashes the app (NullPointerException). Kotlin was designed to prevent this.

By default, **variables cannot be null**.

```kotlin
var name: String = "John"
// name = null // COMPILER ERROR! Kotlin stops the crash before you even run the app.
```

If you *need* a variable to sometimes be empty, you must add a `?` to the type. This creates a "Nullable" type.

```kotlin
var nickname: String? = "Johnny"
nickname = null // Works fine now.
```

But how do you use a nullable variable safely? 
```kotlin
var myName: String? = "Alice"

// 1. Safe Call (`?.`) -> "Only do this if it's NOT null"
val length = myName?.length // If myName is null, length becomes null. App doesn't crash.

// 2. Elvis Operator (`?:`) -> "If it's null, use this default value instead"
val actualLength = myName?.length ?: 0 // If myName is null, actualLength becomes 0.

// 3. Not-Null Assertion (`!!`) -> "I PROMISE it's not null, crash the app if I'm wrong!"
// Avoid using this unless absolutely necessary!
val riskyLength = myName!!.length 
```

---
**Your Turn!**
Take a few minutes to read through this. Open up `MainActivity.kt` and see if you can spot:
1. `val` or `var` (Look for `val notificationManager = ...`)
2. `fun` (Look for `fun WaterReminderApp()` or `fun onCreate(...)`)
3. A `class` declaration (`class MainActivity`)
4. Safe calls using `?` (Hint: Look at `savedInstanceState: Bundle?` in `onCreate`)

Let me know when you feel comfortable with these concepts, and we will move to **Phase 2: Android Project Anatomy**!
