# Reward Earn 3 — Android Application

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)
![UI](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-purple.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Room%20%2B%20Flow-orange.svg)
![Build](https://img.shields.io/badge/Build-Gradle-success.svg)

**Reward Earn 3** is a production-ready, feature-rich Android rewards and earning application tailored for Indian users, built with modern Android standards, 100% Jetpack Compose (Material Design 3), and clean MVVM architecture.

---

## 📱 Features

- **Home Dashboard**: Live coin balance, INR conversion rate (100 Coins = ₹1.00), daily streak rewards, featured tasks, recent transaction history, and instant redeem shortcuts.
- **Super Offers**: Multi-provider task gateway (Torox, PubScale, Lootably, AdGate, CPALead) with category filters (All, Apps, Games, Surveys, Quick Register), task difficulty badges, and reward credit listeners.
- **Play Games & Earn**: Interactive games with level progression and instant coin payouts (Fruit Blast, Bubble Shooter, Carrom King, Temple Run).
- **Watch & Earn**: Rewarded video ad simulation and partner video offerwall with countdown timers and daily watch limits.
- **Refer & Earn**: Unique referral codes, one-tap WhatsApp / Telegram sharing, multi-tier commission rewards, and referral milestone tracking.
- **My Wallet & Redemptions**:
  - Instant UPI Cash (`user@bank`, `phone@upi`)
  - Direct Bank Transfer (IMPS with Account Number & IFSC)
  - Instant Digital Gift Cards (Amazon Pay, Google Play, Flipkart, Paytm)
  - Real-time balance deductions, pending verification queue, and automated transaction ledger.
- **Full In-App Admin Panel**:
  - Protected by secure 4-digit PIN authentication.
  - Live analytics (Total Users, Pending Withdrawals, Total Paid Out, Total Coins in circulation).
  - One-tap approval or rejection of pending withdrawals (with automatic instant coin refunds on rejection).
  - Offer provider management (Enable/Disable Torox, PubScale, Lootably, etc.).
  - Digital gift card inventory vault (inspect claim status and manage voucher codes).
  - System configuration (custom coin conversion rates, minimum payout thresholds, referral bonuses).

---

## 🛠️ Tech Stack & Architecture

- **Language:** Kotlin 100%
- **UI Toolkit:** Jetpack Compose + Material Design 3
- **Architecture:** MVVM + Clean Repository Pattern
- **Reactive Streams:** Kotlin Coroutines & StateFlow
- **Data Persistence:** Android Room Database (TypeConverters, reactive queries, pre-seeded realistic data)
- **Networking & Serialization:** Retrofit2, OkHttp3, Moshi
- **Image Loading:** Coil Compose
- **Build System:** Gradle Kotlin DSL (`build.gradle.kts`) with Version Catalog (`libs.versions.toml`)
- **CI/CD:** GitHub Actions (`.github/workflows/build-apk.yml`)

---

## 🚀 Building & Generating APK

### 1. Build via GitHub Actions (Recommended)
This repository includes a continuous integration workflow at `.github/workflows/build-apk.yml`:
1. Push your commits to `main` branch or trigger manually via the **Actions** tab -> **Build Android APK** -> **Run workflow**.
2. GitHub Actions sets up Java 17, resolves Gradle dependencies, and executes `gradle assembleDebug`.
3. Once the workflow run finishes, download the generated APK from the **Artifacts** section: `RewardEarn3-debug-apk`.

### 2. Build Locally with Gradle
```bash
# Clone the repository
git clone https://github.com/chandarlal772586/Reward-Earn-3.git
cd Reward-Earn-3

# Build Debug APK
gradle assembleDebug

# Output location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 📦 Installing APK on Your Android Device

1. **Transfer or Download the APK**:
   - Download `app-debug.apk` directly to your phone via USB cable, Google Drive, WhatsApp, or browser.
2. **Enable Unknown Sources / Install Unknown Apps**:
   - When opening the `.apk` file on Android, tap **Settings** if prompted:
   - Enable **"Allow from this source"** for your file manager or browser.
3. **Install & Launch**:
   - Tap **Install**. Once completed, tap **Open**.
   - The app launches directly into Reward Earn 3 with all features active.

---

## 🔐 Production Release Signing via GitHub Secrets

To automatically generate a signed release APK in GitHub Actions:
1. In your GitHub repository, navigate to **Settings** > **Secrets and variables** > **Actions**.
2. Add the following repository secrets:
   - `KEYSTORE_BASE64`: Base64 encoded `.jks` or `.keystore` file (`base64 -w 0 your-release-key.jks`).
   - `STORE_PASSWORD`: Password for your keystore.
   - `KEY_ALIAS`: Alias name of your release key.
   - `KEY_PASSWORD`: Password for your key alias.
3. When these secrets are present, the GitHub Actions workflow automatically produces and uploads `RewardEarn3-release-apk`!

---

## 📄 License
Copyright © 2026 Reward Earn 3. All rights reserved.
