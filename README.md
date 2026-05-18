# Namma-Platform

**Namma-Platform** is a Kannada-first railway station assistant Android app built for rural passengers and elderly users at small railway stations. It helps identify upcoming trains, platform numbers, coach sequences, and plays Kannada voice announcements.

## Features

- Splash screen with 2-second auto-navigation
- Firebase Authentication (Login, Register, Forgot Password)
- Home dashboard with station search and next 3 trains
- 6 Karnataka stations with realistic sample data
- Train details with horizontal coach layout (General & Ladies highlighted)
- Kannada Text-to-Speech announcements
- Room database for offline support
- Favorite stations & recent searches
- Profile & Settings (Kannada/English, Dark mode, Notifications)
- Railway chatbot assistant
- Train status indicators (Green/Orange/Red)

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room |
| Auth | Firebase Authentication |
| Navigation | Navigation Compose |
| Async | Coroutines + Flow |
| Data | Local JSON + Room |

## Project Structure

```
app/src/main/java/com/namma/platform/
├── data/           # Room, repositories, JSON loader
├── domain/         # Models & repository interfaces
├── di/             # Hilt modules
├── navigation/     # Nav graph & routes
├── presentation/   # ViewModels & Screens
├── ui/             # Theme & reusable composables
└── utils/          # TTS, preferences, chatbot
```

## Setup Instructions

### 1. Open in Android Studio

1. Open **Android Studio Ladybug (2024.2+)** or newer
2. **File → Open** → select the `Namma_Platform` folder
3. Wait for Gradle sync to complete

### 2. Configure Android SDK

Copy `local.properties.example` to `local.properties` and set your SDK path:

```properties
sdk.dir=C\:\\Users\\YOUR_USERNAME\\AppData\\Local\\Android\\Sdk
```

### 3. Firebase Setup (Required for Authentication)

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project named **Namma-Platform**
3. Add an **Android app** with package name: `com.namma.platform`
4. Download `google-services.json`
5. Replace `app/google-services.json` with your downloaded file
6. In Firebase Console → **Authentication** → **Sign-in method** → Enable **Email/Password**

### 4. Kannada TTS (Device Setup)

On your Android device/emulator:
1. Go to **Settings → System → Languages & input → Text-to-speech**
2. Install **Google Text-to-speech** Kannada language pack
3. Set Kannada as preferred TTS language for best announcement quality

### 5. Build & Run

```bash
./gradlew assembleDebug
```

Or click **Run ▶** in Android Studio on an emulator/device (API 26+).

## Stations Included

| # | Station | Code |
|---|---------|------|
| 1 | Mysuru | MYS |
| 2 | Bengaluru | SBC |
| 3 | Mandya | MYA |
| 4 | Hassan | HAS |
| 5 | Shivamogga | SMET |
| 6 | Hubballi | UBL |

Sample train data is stored in `app/src/main/assets/stations_data.json` and preloaded into Room on first launch.

## UI Theme

- **Primary:** Railway Blue (`#1565C0`)
- **Accent:** Railway Yellow (`#FFC107`)
- Large readable typography for elderly users
- High-contrast cards and status badges

## Offline Support

On first launch, JSON data is parsed and stored in Room. After that, the app works fully offline for station/train browsing. Firebase Auth requires internet only for login/register.

## Sample Kannada Announcement

```
ಮೈಸೂರು ಎಕ್ಸ್‌ಪ್ರೆಸ್ ಪ್ಲಾಟ್‌ಫಾರ್ಮ್ 2 ಕ್ಕೆ ಬರುತ್ತಿದೆ.
ಸಾಮಾನ್ಯ ಬೋಗಿ ಎಂಜಿನ್ ನಂತರ 1ನೇ ಸ್ಥಾನದಲ್ಲಿದೆ.
```

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Firebase login fails | Replace `google-services.json` with your real Firebase file |
| TTS not speaking Kannada | Install Kannada TTS language pack on device |
| Gradle sync fails | Ensure JDK 17 and Android SDK 35 are installed |
| Build error on google-services | Enable Email/Password auth in Firebase Console |

## License

Educational project for MindMatrix VTU Internship Program.
