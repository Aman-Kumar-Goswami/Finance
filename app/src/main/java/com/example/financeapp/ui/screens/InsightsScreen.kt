package com.example.financeapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.financeapp.data.model.Transaction
import com.example.financeapp.data.model.TransactionType
import com.example.financeapp.ui.components.PieChart
import com.example.financeapp.ui.components.SpendingBarChart
import com.example.financeapp.ui.viewmodel.TransactionViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: TransactionViewModel,
    onNavigateBack: () -> Unit
) {
    val transactions by viewModel.allTransactions.collectAsState()
    val selectedCurrency by viewModel.selectedCurrency.collectAsState()
    val currencySymbol = viewModel.currencies.find { it.first == selectedCurrency }?.second ?: "$"
    
    val totalExpense = transactions.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
    
    val highestCategory = transactions
        .filter { it.type == TransactionType.EXPENSE }
        .groupBy { it.category }
        .mapValues { it.value.sumOf { t -> t.amount } }
        .maxByOrNull { it.value }

    val weeklySpending = calculateWeeklySpending(transactions)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Financial Insights") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            item {
                Text(
                    text = "Category Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Expense", style = MaterialTheme.typography.labelLarge, color = Color(0xFFF44336))
                            Spacer(modifier = Modifier.height(12.dp))
                            PieChart(
                                transactions = transactions.filter { it.type == TransactionType.EXPENSE },
                                currencySymbol = currencySymbol,
                                modifier = Modifier.size(100.dp),
                                isExpense = true
                            )
                        }
                    }
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp)) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Income", style = MaterialTheme.typography.labelLarge, color = Color(0xFF4CAF50))
                            Spacer(modifier = Modifier.height(12.dp))
                            PieChart(
                                transactions = transactions.filter { it.type == TransactionType.INCOME },
                                currencySymbol = currencySymbol,
                                modifier = Modifier.size(100.dp),
                                isExpense = false
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Weekly Spending Trend",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
                SpendingBarChart(
                    transactions = transactions,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Text(
                    text = "Key Patterns",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
                Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                    InsightStatCard(
                        title = "Top Category",
                        value = highestCategory?.key ?: "N/A",
                        icon = Icons.Default.PieChart,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    InsightStatCard(
                        title = "This Week",
                        value = "$currencySymbol${String.format(Locale.getDefault(), "%.2f", weeklySpending)}",
                        icon = if (weeklySpending > 0) Icons.AutoMirrored.Filled.TrendingUp else Icons.AutoMirrored.Filled.TrendingDown,
                        color = if (weeklySpending > 0) Color(0xFFF44336) else Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            val categoryTotals = transactions
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.category }
                .mapValues { it.value.sumOf { t -> t.amount } }
                .toList()
                .sortedByDescending { it.second }

            if (categoryTotals.isNotEmpty()) {
                item {
                    Text(
                        text = "Spending Details",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                itemsIndexed(categoryTotals) { index, categoryTotal ->
                    val (category, amount) = categoryTotal
                    val percentage = if (totalExpense > 0) (amount / totalExpense * 100).toInt() else 0
                    CategoryInsightRow(category, amount, percentage, currencySymbol, index)
                }
            }
        }
    }
}

@Composable
fun InsightStatCard(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = color)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
fun CategoryInsightRow(category: String, amount: Double, percentage: Int, currencySymbol: String, index: Int) {
    val colors = listOf(
        Color(0xFFF44336), Color(0xFF2196F3), Color(0xFF4CAF50),
        Color(0xFFFFEB3B), Color(0xFFFF9800), Color(0xFF9C27B0),
        Color(0xFF00BCD4), Color(0xFF795548)
    )
    val indicatorColor = colors[index % colors.size]

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(indicatorColor))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = category, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    Text(text = "$percentage% of total spending", style = MaterialTheme.typography.bodySmall)
                }
            }
            Text(
                text = "$currencySymbol${String.format(Locale.getDefault(), "%.2f", amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFF44336)
            )
        }
    }
}

private fun calculateWeeklySpending(transactions: List<Transaction>): Double {
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    val startOfWeek = cal.timeInMillis
    
    return transactions
        .filter { it.type == TransactionType.EXPENSE && it.date >= startOfWeek }
        .sumOf { it.amount }
}
