package com.example.financeapp.ui.viewmodel

import com.example.financeapp.data.model.Transaction

data class HomeUiState(
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val savingsGoal: Double = 1000.0,
    val savingsProgress: Float = 0f,
    val noSpendStreak: Int = 0,
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false
)
