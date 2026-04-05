package com.example.financeapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.financeapp.data.local.AppDatabase
import com.example.financeapp.data.model.Transaction
import com.example.financeapp.data.model.TransactionType
import com.example.financeapp.data.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TransactionRepository
    private val sharedPrefs = application.getSharedPreferences("finance_prefs", Context.MODE_PRIVATE)

    private val _savingsGoal = MutableStateFlow(sharedPrefs.getFloat("savings_goal", 1000f).toDouble())
    val savingsGoal: StateFlow<Double> = _savingsGoal.asStateFlow()

    private val _userName = MutableStateFlow(sharedPrefs.getString("user_name", "John Doe") ?: "John Doe")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _isRotationLocked = MutableStateFlow(sharedPrefs.getBoolean("rotation_locked", false))
    val isRotationLocked: StateFlow<Boolean> = _isRotationLocked.asStateFlow()

    private val _selectedCurrency = MutableStateFlow(sharedPrefs.getString("selected_currency", "INR") ?: "INR")
    val selectedCurrency: StateFlow<String> = _selectedCurrency.asStateFlow()

    val currencies = listOf(
        "USD" to "$",
        "INR" to "₹",
        "EUR" to "€",
        "GBP" to "£",
        "JPY" to "¥",
        "AED" to "د.إ"
    )

    // Dynamic Categories
    private val defaultExpenseCategories = setOf("Food", "Transport", "Shopping", "Rent", "Bills", "Health", "Other")
    private val defaultIncomeCategories = setOf("Salary", "Investment", "Gift", "Bonus", "Other")

    private val _expenseCategories = MutableStateFlow(sharedPrefs.getStringSet("expense_categories", defaultExpenseCategories)?.toList()?.sorted() ?: defaultExpenseCategories.toList().sorted())
    val expenseCategories: StateFlow<List<String>> = _expenseCategories.asStateFlow()

    private val _incomeCategories = MutableStateFlow(sharedPrefs.getStringSet("income_categories", defaultIncomeCategories)?.toList()?.sorted() ?: defaultIncomeCategories.toList().sorted())
    val incomeCategories: StateFlow<List<String>> = _incomeCategories.asStateFlow()

    val allTransactions: StateFlow<List<Transaction>>
    val homeUiState: StateFlow<HomeUiState>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repository = TransactionRepository(dao)

        allTransactions = repository.allTransactions
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        homeUiState = combine(
            allTransactions,
            _savingsGoal,
            _userName
        ) { transactions, goal, name ->
            val income = transactions.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expenses = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            val balance = income - expenses
            val progress = if (goal > 0) (balance.coerceAtLeast(0.0) / goal).toFloat().coerceIn(0f, 1f) else 0f
            
            HomeUiState(
                balance = balance,
                totalIncome = income,
                totalExpenses = expenses,
                savingsGoal = goal,
                savingsProgress = progress,
                noSpendStreak = calculateStreak(transactions),
                recentTransactions = transactions.take(5),
                isLoading = false
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState(isLoading = true))
    }

    private fun calculateStreak(transactions: List<Transaction>): Int {
        if (transactions.isEmpty()) return 0
        val expenseDates = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .map { 
                val cal = Calendar.getInstance()
                cal.timeInMillis = it.date
                cal.apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }.toSet()

        var streak = 0
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        
        cal.add(Calendar.DAY_OF_YEAR, -1)
        while (streak <= 365) {
            if (!expenseDates.contains(cal.timeInMillis)) {
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else break
        }
        return streak
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.insert(transaction) }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.update(transaction) }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch { repository.delete(transaction) }
    }

    fun clearAllData() {
        viewModelScope.launch { repository.deleteAll() }
    }

    suspend fun getTransactionById(id: Int): Transaction? = repository.getTransactionById(id)

    fun setSavingsGoal(goal: Double) {
        _savingsGoal.value = goal
        sharedPrefs.edit().putFloat("savings_goal", goal.toFloat()).apply()
    }

    fun setUserName(name: String) {
        _userName.value = name
        sharedPrefs.edit().putString("user_name", name).apply()
    }

    fun toggleRotationLock(isLocked: Boolean) {
        _isRotationLocked.value = isLocked
        sharedPrefs.edit().putBoolean("rotation_locked", isLocked).apply()
    }

    fun setCurrency(newCurrencyCode: String) {
        if (_selectedCurrency.value == newCurrencyCode) return
        
        _selectedCurrency.value = newCurrencyCode
        sharedPrefs.edit().putString("selected_currency", newCurrencyCode).apply()
        
        viewModelScope.launch {
            val transactions = allTransactions.value
            transactions.forEach { transaction ->
                if (transaction.currencyCode != newCurrencyCode) {
                    repository.update(transaction.copy(currencyCode = newCurrencyCode))
                }
            }
        }
    }

    fun addCategory(name: String, type: TransactionType) {
        val trimmedName = name.trim()
        val currentCategories = if (type == TransactionType.EXPENSE) _expenseCategories.value else _incomeCategories.value
        
        val exists = currentCategories.any { it.equals(trimmedName, ignoreCase = true) }
        
        if (!exists && trimmedName.isNotBlank()) {
            if (type == TransactionType.EXPENSE) {
                val newList = (_expenseCategories.value + trimmedName).sorted()
                _expenseCategories.value = newList
                sharedPrefs.edit().putStringSet("expense_categories", newList.toSet()).apply()
            } else {
                val newList = (_incomeCategories.value + trimmedName).sorted()
                _incomeCategories.value = newList
                sharedPrefs.edit().putStringSet("income_categories", newList.toSet()).apply()
            }
        }
    }

    fun deleteCategory(name: String, type: TransactionType) {
        if (type == TransactionType.EXPENSE) {
            val newList = _expenseCategories.value.filter { it != name }.sorted()
            _expenseCategories.value = newList
            sharedPrefs.edit().putStringSet("expense_categories", newList.toSet()).apply()
        } else {
            val newList = _incomeCategories.value.filter { it != name }.sorted()
            _incomeCategories.value = newList
            sharedPrefs.edit().putStringSet("income_categories", newList.toSet()).apply()
        }
    }
}
