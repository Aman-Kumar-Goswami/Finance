package com.example.financeapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val currencyCode: String = "USD"
)

enum class TransactionType {
    INCOME, EXPENSE
}
