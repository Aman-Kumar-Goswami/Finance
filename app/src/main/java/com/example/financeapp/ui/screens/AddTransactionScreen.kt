package com.example.financeapp.ui.screens

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financeapp.data.model.Transaction
import com.example.financeapp.data.model.TransactionType
import com.example.financeapp.ui.viewmodel.TransactionViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun AddTransactionScreen(
    viewModel: TransactionViewModel,
    transactionId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val selectedCurrencyCode by viewModel.selectedCurrency.collectAsState()
    
    var quickInput by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(TransactionType.EXPENSE) }
    var activeCurrencyCode by remember { mutableStateOf(selectedCurrencyCode) }
    val activeCurrencySymbol = viewModel.currencies.find { it.first == activeCurrencyCode }?.second ?: "$"
    
    var amountError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<String?>(null) }

    // Update active currency when global selection changes
    LaunchedEffect(selectedCurrencyCode) {
        activeCurrencyCode = selectedCurrencyCode
    }

    // Load existing transaction if editing
    LaunchedEffect(transactionId) {
        if (transactionId != null && transactionId != -1) {
            viewModel.getTransactionById(transactionId)?.let {
                amount = it.amount.toString()
                category = it.category
                note = it.note
                type = it.type
                activeCurrencyCode = it.currencyCode
            }
        }
    }

    val expenseCategories by viewModel.expenseCategories.collectAsState()
    val incomeCategories by viewModel.incomeCategories.collectAsState()
    val categories = if (type == TransactionType.EXPENSE) expenseCategories else incomeCategories

    // AI Keywords Mapping
    val categoryKeywords = mapOf(
        "Food" to listOf("burger", "pizza", "dinner", "lunch", "breakfast", "coffee", "tea", "eat", "food", "maggi", "snacks"),
        "Transport" to listOf("uber", "taxi", "bus", "train", "fuel", "petrol", "gas", "ola", "auto", "fare"),
        "Shopping" to listOf("amazon", "clothes", "shoes", "electronics", "flipkart", "myntra", "shopping"),
        "Salary" to listOf("salary", "pay", "income", "stipend"),
        "Bills" to listOf("electricity", "water", "wifi", "recharge", "bill", "rent")
    )

    fun parseInput(input: String) {
        val lowerInput = input.lowercase()
        val amountRegex = Regex("""\d+([,.]\d+)*""")
        val matches = amountRegex.findAll(input).map { it.value }.toList()
        
        if (matches.isNotEmpty()) {
            val probableAmount = matches.maxByOrNull { it.length } ?: ""
            amount = probableAmount.replace(",", "")
            amountError = false
        }
        
        if (lowerInput.contains("received") || lowerInput.contains("salary") || lowerInput.contains("income") || lowerInput.contains("got")) {
            type = TransactionType.INCOME
        } else { type = TransactionType.EXPENSE }

        for ((cat, keywords) in categoryKeywords) {
            if (keywords.any { lowerInput.contains(it) }) {
                category = cat
                categoryError = false
                break
            }
        }
        note = input
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0) ?: ""
                quickInput = spokenText
                parseInput(spokenText)
            }
        }
    )

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("Delete Category") },
            text = { Text("Are you sure you want to delete '${categoryToDelete}' category?") },
            confirmButton = {
                Button(
                    onClick = {
                        categoryToDelete?.let { viewModel.deleteCategory(it, type) }
                        if (category == categoryToDelete) {
                            category = ""
                        }
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transactionId == null || transactionId == -1) "Smart Entry" else "Edit Transaction") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            if (transactionId == null || transactionId == -1) {
                OutlinedTextField(
                    value = quickInput,
                    onValueChange = { quickInput = it; parseInput(it) },
                    label = { Text("Say something (e.g. add burger 250)") },
                    leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    trailingIcon = {
                        IconButton(onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                            }
                            speechLauncher.launch(intent)
                        }) {
                            Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider()
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text("Transaction Details", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(selected = type == TransactionType.EXPENSE, onClick = { type = TransactionType.EXPENSE }, shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)) { Text("Expense") }
                SegmentedButton(selected = type == TransactionType.INCOME, onClick = { type = TransactionType.INCOME }, shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)) { Text("Income") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Currency Selection Row
            Text("Select Currency", style = MaterialTheme.typography.labelSmall)
            LazyRow(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.currencies) { pair ->
                    val code = pair.first
                    val symbol = pair.second
                    FilterChip(
                        selected = activeCurrencyCode == code,
                        onClick = { activeCurrencyCode = code },
                        label = { Text("$symbol ($code)") }
                    )
                }
            }

            OutlinedTextField(
                value = amount,
                onValueChange = { 
                    amount = it
                    amountError = it.toDoubleOrNull() == null || it.toDouble() <= 0 
                },
                label = { Text("Amount") },
                prefix = { Text("$activeCurrencySymbol ") },
                isError = amountError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )
            if (amountError) {
                Text("Please enter a valid amount", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Selector
            Text("Category", style = MaterialTheme.typography.labelLarge, color = if (categoryError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
            FlowRow(modifier = Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categories.forEach { cat ->
                    val isSelected = category == cat
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .combinedClickable(
                                onClick = {
                                    category = cat
                                    categoryError = false
                                },
                                onLongClick = { categoryToDelete = cat }
                            )
                    ) {
                        Text(
                            text = cat,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
                
                // Add Category Button
                var showAddCategoryDialog by remember { mutableStateOf(false) }
                var newCategoryName by remember { mutableStateOf("") }
                
                if (showAddCategoryDialog) {
                    AlertDialog(
                        onDismissRequest = { showAddCategoryDialog = false },
                        title = { Text("Add Category") },
                        text = {
                            OutlinedTextField(
                                value = newCategoryName,
                                onValueChange = { newCategoryName = it },
                                label = { Text("Category Name") }
                            )
                        },
                        confirmButton = {
                            Button(onClick = {
                                if (newCategoryName.isNotBlank()) {
                                    viewModel.addCategory(newCategoryName, type)
                                    category = newCategoryName
                                    newCategoryName = ""
                                    showAddCategoryDialog = false
                                }
                            }) { Text("Add") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showAddCategoryDialog = false }) { Text("Cancel") }
                        }
                    )
                }
                
                IconButton(onClick = { showAddCategoryDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Category")
                }
            }
            if (categoryError) {
                Text("Please select a category", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    val isAmountValid = amountValue > 0
                    val isCategoryValid = category.isNotEmpty()
                    
                    amountError = !isAmountValid
                    categoryError = !isCategoryValid
                    
                    if (isAmountValid && isCategoryValid) {
                        val transaction = Transaction(
                            id = if (transactionId != null && transactionId != -1) transactionId else 0,
                            amount = amountValue,
                            type = type,
                            category = category,
                            note = note,
                            currencyCode = activeCurrencyCode
                        )
                        if (transaction.id == 0) {
                            viewModel.addTransaction(transaction)
                            Toast.makeText(context, "Transaction Saved!", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.updateTransaction(transaction)
                            Toast.makeText(context, "Transaction Updated!", Toast.LENGTH_SHORT).show()
                        }
                        onNavigateBack()
                    } else {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(if (transactionId == null || transactionId == -1) "Save Transaction" else "Update Transaction")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
