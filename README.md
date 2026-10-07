# 💰 Finora – Personal Finance Manager

**Finora** is a modern personal finance management Android application built with **Kotlin, Jetpack Compose, Material 3, Room Database, and MVVM architecture**.

It helps users manage their income and expenses, visualize spending patterns, set savings goals, organize categories, and maintain their financial records locally and securely.

---

## ✨ Features

### 🧾 Smart Transaction Management

* Add **income and expense** transactions quickly.
* Add transaction notes and additional details.
* Voice-based transaction entry using **Speech-to-Text**.
* Intelligent text parsing to identify transaction amounts and categories.
* Edit and delete transactions.
* Undo recently deleted transactions.
* Confirmation before clearing all financial data.

### 📊 Financial Dashboard & Insights

* Income vs. Expense **Pie Charts**.
* Weekly spending **Bar Charts**.
* Category-wise expense analysis.
* Visual representation of financial activity.
* Real-time updates when transactions are added or removed.
* Clean and color-coded financial information.

### 🗂️ Dynamic Category Management

* Create custom income and expense categories.
* Delete unwanted categories using long-press.
* Confirmation dialog before category deletion.
* Prevent duplicate categories using **case-insensitive validation**.
* Separate category management for income and expenses.

### 💱 Multi-Currency Support

Finora supports multiple currencies:

| Currency           | Code |
| ------------------ | ---- |
| 🇺🇸 US Dollar     | USD  |
| 🇮🇳 Indian Rupee  | INR  |
| 🇪🇺 Euro          | EUR  |
| 🇬🇧 British Pound | GBP  |
| 🇯🇵 Japanese Yen  | JPY  |
| 🇦🇪 UAE Dirham    | AED  |

Currency symbols are automatically reflected throughout the application.

### 🎯 Savings Goals

* Set monthly savings targets.
* Track current savings progress.
* Monitor progress directly from the dashboard.
* Compare income, expenses, and savings performance.

### 👤 Personalized Experience

* Customize the application with your name.
* Light and Dark theme support.
* Modern **Material 3** interface.
* Responsive Jetpack Compose UI.
* Simple and user-friendly navigation.

### 🔔 Daily Transaction Reminders

* Schedule daily reminders to record transactions.
* Uses Android **AlarmManager** for scheduled notifications.
* Notification permission is requested when required.
* Helps users maintain consistent financial records.

### 🛡️ Local Data & Safety

* Financial data is stored locally using **Room Database**.
* No financial records need to be uploaded to a remote server.
* Undo support for recently deleted transactions.
* Confirmation before deleting all data.
* Data remains available across app restarts.

---

# 🛠️ Tech Stack

| Category         | Technology                                 |
| ---------------- | ------------------------------------------ |
| Language         | Kotlin                                     |
| UI Toolkit       | Jetpack Compose                            |
| Design System    | Material 3                                 |
| Architecture     | MVVM                                       |
| State Management | Kotlin Flow / StateFlow                    |
| Database         | Room Database                              |
| Navigation       | Jetpack Compose Navigation                 |
| Voice Input      | Android Speech Recognition / Voice-to-Text |
| Notifications    | Android AlarmManager                       |
| Charts           | Compose-based Data Visualization           |
| Build Tool       | Gradle                                     |
| IDE              | Android Studio                             |

---

# 🏗️ Architecture

Finora follows the **MVVM (Model–View–ViewModel)** architecture with a repository layer to maintain separation between UI, business logic, and data management.

```text
┌──────────────────────────────┐
│       Jetpack Compose        │
│             UI               │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│          ViewModel           │
│      StateFlow / Logic       │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│         Repository           │
│       Data Management        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│        Room Database         │
│        Local Storage         │
└──────────────────────────────┘
```

### Architecture Flow

```text
User Interaction
       ↓
Jetpack Compose UI
       ↓
ViewModel
       ↓
Repository
       ↓
Room DAO
       ↓
Local Database
```

This structure keeps the UI independent from the data layer and makes the application easier to maintain, test, and extend.

---

# 📱 Screenshots

## 🏠 Home Dashboard

![Finora Home](screenshots/home.png)

The home dashboard provides a quick overview of income, expenses, savings, and recent transactions.

---

## 🎙️ Smart Transaction Entry

![Smart Entry](screenshots/smart_entry.png)

Users can enter transactions using voice input and quickly record financial activity.

---

## 📊 Financial Insights

![Financial Insights](screenshots/insights.png)

Visual charts help users understand their spending patterns and financial performance.

---

## ⚙️ Settings

![Finora Settings](screenshots/settings.png)

Manage preferences such as currency, theme, user information, categories, and reminders.

---

# 🚀 Getting Started

## 1. Clone the Repository

```bash
git clone https://github.com/Aman-Kumar-Goswami/Finance.git
```

## 2. Open the Project

Open the project in **Android Studio**.

## 3. Sync Gradle

Allow Android Studio to download and synchronize all required dependencies.

## 4. Run the Application

Connect an Android device or start an emulator and run the application.

---

# 📋 Requirements

* Android Studio **Ladybug or newer**
* Android SDK
* Android device or emulator
* JDK compatible with the project configuration
* Internet connection for the initial Gradle dependency download

---

# 🔐 Permissions

Finora requests permissions only when the related functionality is used.

### 🎙️ Microphone

Required for voice-based transaction entry.

### 🔔 Notifications

Required for daily transaction reminders on Android versions that require notification permission.

---

# 🎯 Project Highlights

Finora demonstrates practical implementation of modern Android development concepts:

* ✅ Kotlin-based Android development
* ✅ Jetpack Compose UI
* ✅ Material 3 design
* ✅ MVVM architecture
* ✅ Repository pattern
* ✅ Room Database
* ✅ Kotlin Flow and StateFlow
* ✅ Compose Navigation
* ✅ Financial data visualization
* ✅ Voice-to-Text transaction entry
* ✅ Intelligent transaction parsing
* ✅ Dynamic category management
* ✅ Multi-currency support
* ✅ Savings goal tracking
* ✅ Android AlarmManager
* ✅ Scheduled notifications
* ✅ Light and Dark theme
* ✅ Local-first data management

---

# 🔮 Future Improvements

The following features are planned for future versions:

* ☁️ Cloud data synchronization
* 🔐 User authentication
* 🌐 Web dashboard
* 📱 Multi-device synchronization
* 📩 Automatic transaction detection from bank/UPI SMS
* 📤 CSV and PDF financial reports
* 🤖 AI-powered financial insights
* 💳 Account and card management
* 📈 Advanced financial analytics
* 🔄 Cloud backup and restore

---

# 🤝 Contributing

Contributions, suggestions, and feature requests are welcome.

### Contribution Steps

1. Fork the repository.
2. Create a new feature branch.
3. Make your changes.
4. Commit your changes.
5. Push the branch.
6. Open a Pull Request.

---

# 👨‍💻 Author

## Aman Kumar

**Android Developer | Kotlin | Jetpack Compose**

* GitHub: `Aman-Kumar-Goswami`
* Project: `Finance / Finora`

---

# ⭐ Support

If you find **Finora** useful, consider giving the repository a ⭐ on GitHub.

Your feedback and suggestions are always welcome.

---

# 📄 License

This project is licensed under the **MIT License**.

---

<div align="center">

### Made with ❤️ using Kotlin & Jetpack Compose

**Finora — Take control of your finances.**

</div>
