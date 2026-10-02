# 💰 Finora – Personal Finance Manager

**Finora** is a modern and intuitive personal finance management application built with **Kotlin and Jetpack Compose**. It helps users track income and expenses, analyze spending patterns, manage savings goals, and maintain their financial records securely.

---

## ✨ Features

### 🧾 Smart Transaction Entry

* Quickly add income and expense transactions.
* Voice-based transaction entry using Voice-to-Text.
* Intelligent text parsing to detect transaction amounts and categories.
* Add transaction notes and details.

### 📊 Financial Insights

* Dual Pie Charts for **Income vs. Expenses**.
* Weekly spending trends using Bar Charts.
* Category-wise spending analysis.
* Color-coded financial information for better visualization.

### 🗂️ Dynamic Category Management

* Add custom income and expense categories.
* Long-press to delete unwanted categories.
* Confirmation dialog before deleting categories.
* Prevents duplicate categories using case-insensitive validation.

### 💱 Multi-Currency Support

Supports multiple currencies:

* 🇺🇸 USD
* 🇮🇳 INR
* 🇪🇺 EUR
* 🇬🇧 GBP
* 🇯🇵 JPY
* 🇦🇪 AED

Currency symbols are automatically updated throughout the application.

### 🎯 Savings Goals

* Set monthly savings goals.
* Track current savings progress.
* Monitor financial progress directly from the dashboard.

### 👤 Personalized Experience

* Customize the application with your name.
* Light and Dark Mode support.
* Clean and responsive Material 3 interface.

### 🔔 Daily Reminders

* Automated daily reminders to record transactions.
* Uses Android AlarmManager for scheduled notifications.
* Helps users maintain consistent financial records.

### 🛡️ Data Safety

* Local data persistence using Room Database.
* Undo option for recently deleted transactions.
* Confirmation dialog before clearing all data.
* All financial records remain stored locally on the device.

---

## 🛠️ Tech Stack

| Category         | Technology                         |
| ---------------- | ---------------------------------- |
| Language         | Kotlin                             |
| UI               | Jetpack Compose                    |
| Design           | Material 3                         |
| Architecture     | MVVM                               |
| Database         | Room Database                      |
| Navigation       | Jetpack Compose Navigation         |
| State Management | Kotlin Flow & StateFlow            |
| Notifications    | AlarmManager                       |
| Voice Input      | Android Speech / Voice-to-Text API |
| Build Tool       | Gradle                             |
| IDE              | Android Studio                     |

---

## 🏗️ Architecture

Finora follows the **MVVM (Model–View–ViewModel)** architecture to maintain a clean separation between UI, business logic, and data.

```text
┌─────────────────────────┐
│      Jetpack Compose    │
│           UI            │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       ViewModel         │
│   StateFlow / Logic     │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Repository        │
│     Data Management     │
└────────────┬────────────┘
             │
             ▼
┌─────────────────────────┐
│       Room Database     │
│      Local Storage      │
└─────────────────────────┘
```

---

## 📱 Screenshots

### 🏠 Home

![Home Screen](screenshots/home.png)

### 🎙️ Smart Entry

![Smart Entry](screenshots/smart_entry.png)

### 📊 Financial Insights

![Financial Insights](screenshots/insights.png)

### ⚙️ Settings

![Settings](screenshots/settings.png)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Aman-Kumar-Goswami/Finance.git
```

### 2. Open the Project

Open the cloned project in **Android Studio**.

### 3. Sync Gradle

Allow Android Studio to sync all Gradle dependencies.

### 4. Run the Application

Connect an Android device or start an emulator and run the application.

---

## 📋 Requirements

* Android Studio **Ladybug or newer**
* Android SDK
* Android device or emulator
* Internet connection for initial dependency downloads

---

## 🔐 Permissions

Depending on the enabled features, the application may require permissions for:

* 🎙️ Microphone — Voice-to-Text transaction entry
* 🔔 Notifications — Daily transaction reminders

Permissions are requested only when the related feature requires them.

---

## 🎯 Project Highlights

Finora demonstrates practical implementation of:

* Modern Android UI development with Jetpack Compose.
* MVVM architecture.
* Local database management with Room.
* Reactive state management using StateFlow.
* Compose Navigation.
* Financial data visualization.
* Voice-based user interaction.
* Scheduled Android notifications.
* Dynamic category management.
* Multi-currency handling.
* Dark theme support.

---

## 🔮 Future Improvements

Planned improvements include:

* ☁️ Cloud synchronization.
* 🔐 User authentication.
* 🌐 Web dashboard.
* 📩 Automatic transaction detection from bank/UPI SMS.
* 📤 CSV/PDF financial reports.
* 🤖 AI-powered financial insights.
* 💳 Account and card management.
* 🔄 Multi-device synchronization.

---

## 🤝 Contributing

Contributions, suggestions, and feature requests are welcome.

To contribute:

1. Fork the repository.
2. Create a new branch.
3. Make your changes.
4. Commit your changes.
5. Push the branch.
6. Create a Pull Request.

---

## 👨‍💻 Author

### Aman Kumar

**Android Developer | Kotlin | Jetpack Compose**

* GitHub: [Aman-Kumar-Goswami](https://github.com/Aman-Kumar-Goswami)
* Project Repository: [Finora – FinanceApp](https://github.com/Aman-Kumar-Goswami/Finance)

---

## ⭐ Support

If you find **Finora** useful, consider giving the repository a ⭐ on GitHub.

---

## 📄 License

This project is licensed under the **MIT License**.

---

<p align="center">
  Made with ❤️ using Kotlin & Jetpack Compose
</p>
