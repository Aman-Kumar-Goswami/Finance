# FinanceApp 💰

FinanceApp is a modern, intuitive personal finance manager built with Jetpack Compose. It helps users track their income and expenses, visualize spending patterns, and manage their monthly savings goals with ease.

## ✨ Features

- **Smart Entry**: Quickly add transactions using voice commands or intelligent text parsing that automatically detects amounts and categories.
- **Financial Insights**:
    - Dual Pie Charts for a clear breakdown of Income vs. Expenses.
    - Weekly spending trends with Bar Charts.
    - Color-coded category details for better visibility.
- **Dynamic Category Management**:
    - Add custom categories on the fly.
    - Long-press to delete unwanted categories with a safety confirmation.
    - Prevents duplicates (case-insensitive checks).
- **Multi-Currency Support**: Switch between USD, INR, EUR, GBP, JPY, and AED. The app automatically updates symbols across all screens.
- **Personalized Experience**:
    - **Dark Mode**: Seamlessly toggle between light and dark themes.
    - **Savings Goals**: Set and track your monthly savings progress.
    - **User Profile**: Customize the app with your name.
- **Daily Reminders**: Automated notifications to remind you to log your transactions every evening.
- **Data Safety**:
    - "Undo" option for recently deleted transactions.
    - Secure local storage using Room Database.
    - Option to clear all data with a confirmation dialog.

## 🛠 Tech Stack

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: [Room Database](https://developer.android.com/training/data-storage/room) for local persistence.
- **Navigation**: [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)
- **State Management**: Kotlin Flows & StateFlow
- **Features**: Voice-to-Text API, AlarmManager for Notifications.

## 🚀 Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/FinanceApp.git
   ```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync the project with Gradle files.
4. Run the app on an emulator or a physical device.

## 📸 Screenshots

<p align="center">
  <img src="screenshots/home.png" width="24%" />
  <img src="screenshots/smart_entry.png" width="24%" />
  <img src="screenshots/insights.png" width="24%" />
  <img src="screenshots/settings.png" width="24%" />
</p>

## 📄 License

This project is licensed under the MIT License.
