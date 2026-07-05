# Tasks — Compose Multiplatform Todo & Task Manager

A task management and todo-list app with a **single shared Kotlin/Compose UI** running on:

- **Android** (Jetpack Compose)
- **Web** (Compose Multiplatform for Kotlin/Wasm)

## Features

- Create, edit, and delete tasks
- Mark tasks complete / active
- Priorities (Low / Medium / High)
- Due dates (Today / Tomorrow / +1 week)
- Full-text search
- Filter: All / Active / Done
- Sort: Newest / Due date / Priority / A–Z
- Clear all completed
- Local persistence — SharedPreferences on Android, `localStorage` on Web
- Light & dark theme

## Project layout

```
composeApp/
  src/commonMain/   # shared UI, models, view model, repository (all platforms)
  src/androidMain/  # Android Activity, Application, SharedPreferences storage
  src/wasmJsMain/   # Web entry point, localStorage storage, index.html
```

The shared code lives in `commonMain`; each platform only provides a thin
storage implementation (`expect`/`actual`) and an entry point.

## Requirements

- JDK 17
- Android SDK (compileSdk 34) for the Android target

## Build & run

### Android

```bash
./gradlew :composeApp:assembleDebug
# APK: composeApp/build/outputs/apk/debug/composeApp-debug.apk
```

Install on a device/emulator:

```bash
./gradlew :composeApp:installDebug
```

### Web (Kotlin/Wasm)

Run a hot-reloading dev server:

```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

Produce a static production bundle:

```bash
./gradlew :composeApp:wasmJsBrowserDistribution
# Output: composeApp/build/dist/wasmJs/productionExecutable/
```

Serve that directory with any static file server to host the website.

## Tech stack

- Kotlin Multiplatform 2.1.0
- Compose Multiplatform 1.7.3
- Material 3
- kotlinx-serialization / kotlinx-datetime / kotlinx-coroutines
- AndroidX Lifecycle ViewModel
