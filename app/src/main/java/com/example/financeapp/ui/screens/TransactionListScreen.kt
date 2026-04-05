package com.example.financeapp.ui.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.financeapp.ui.components.TransactionItem
import com.example.financeapp.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    viewModel: TransactionViewModel,
    onNavigateToEdit: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val transactions by viewModel.allTransactions.collectAsState()
    
    var searchQuery by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf<Int?>(null) } // null means All
    var selectedDate by remember { mutableStateOf<Long?>(null) } // null means All

    val months = listOf("All", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val filteredTransactions = transactions.filter { transaction ->
        val matchesSearch = transaction.category.contains(searchQuery, ignoreCase = true) ||
                transaction.note.contains(searchQuery, ignoreCase = true)
        
        val cal = Calendar.getInstance().apply { timeInMillis = transaction.date }
        
        val matchesMonth = if (selectedMonth == null || selectedMonth == 0) true 
                          else cal.get(Calendar.MONTH) == (selectedMonth!! - 1)
        
        val matchesDate = if (selectedDate == null) true 
                         else {
                             val filterCal = Calendar.getInstance().apply { timeInMillis = selectedDate!! }
                             cal.get(Calendar.YEAR) == filterCal.get(Calendar.YEAR) &&
                             cal.get(Calendar.DAY_OF_YEAR) == filterCal.get(Calendar.DAY_OF_YEAR)
                         }

        matchesSearch && matchesMonth && matchesDate
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Transactions") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(context, { _, year, month, day ->
                            val selected = Calendar.getInstance()
                            selected.set(year, month, day)
                            selectedDate = selected.timeInMillis
                            selectedMonth = 0 // Reset month filter if specific date is picked
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                    }
                    if (selectedDate != null || (selectedMonth != null && selectedMonth != 0)) {
                        TextButton(onClick = { 
                            selectedDate = null
                            selectedMonth = 0 
                        }) {
                            Text("Clear")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search category or note...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            // Month Filter (Scrollable Row)
            Text(
                text = "Filter by Month", 
                style = MaterialTheme.typography.labelMedium, 
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(months.size) { index ->
                    FilterChip(
                        selected = (selectedMonth ?: 0) == index,
                        onClick = { 
                            selectedMonth = index
                            selectedDate = null // Reset specific date if month is picked
                        },
                        label = { Text(months[index]) }
                    )
                }
            }

            if (selectedDate != null) {
                val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                AssistChip(
                    onClick = { },
                    label = { Text("Date: ${formatter.format(Date(selectedDate!!))}") },
                    modifier = Modifier.padding(horizontal = 16.dp),
                    trailingIcon = {
                        Icon(
                            Icons.Default.FilterList,
                            contentDescription = null,
                            modifier = Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // Transactions List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (filteredTransactions.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "No transactions match your filters.")
                        }
                    }
                } else {
                    items(filteredTransactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            currencies = viewModel.currencies,
                            onDelete = { viewModel.deleteTransaction(transaction) },
                            modifier = Modifier.clickable { onNavigateToEdit(transaction.id) }
                        )
                    }
                }
            }
        }
    }
}
