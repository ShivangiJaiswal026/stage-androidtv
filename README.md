# stage-androidtv
🎬 Stage Android TV App – A sample OTT application built for Android TV using Kotlin, Jetpack Compose, and MVVM architecture.
It showcases category-based content browsing, video playback, and adaptive UI for TV devices.
Designed to demonstrate clean architecture, responsive layouts, and seamless viewing experience optimized for large screens.

---

## 🔧 Tech Stack
- **Kotlin** – primary language
- **Jetpack Compose** – declarative UI
- **MVVM** – clean architecture pattern
- **Retrofit** – for API handling
- **ExoPlayer** – for smooth video playback
- **Coroutines + ViewModel** – for lifecycle-safe async loading

---

## 📺 Features
- Movie grid with thumbnails and focus support
- Detail screen with movie info and Play option
- ExoPlayer-powered playback with seek and pause
- Optimized for Android TV navigation

---

## 🚀 How to Run
1. Clone this repository:
   git clone https://github.com/<your-username>/stage-androidtv.git
Open the project in Android Studio (Flamingo or newer).

Choose an Android TV Emulator or physical TV device.

Build and run the app.

🧩 Testing
Includes unit tests for ViewModel and Repository layers

UI tests check basic navigation and playback

Run tests with:

./gradlew test connectedAndroidTest


