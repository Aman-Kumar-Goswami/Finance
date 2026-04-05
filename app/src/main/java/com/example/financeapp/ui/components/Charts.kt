package com.example.financeapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.financeapp.data.model.Transaction
import com.example.financeapp.data.model.TransactionType
import java.util.*

@Composable
fun PieChart(
    transactions: List<Transaction>,
    currencySymbol: String,
    modifier: Modifier = Modifier,
    isExpense: Boolean = true
) {
    val totalAmount = transactions.sumOf { it.amount }
    val categoryTotals = transactions.groupBy { it.category }.mapValues { it.value.sumOf { t -> t.amount } }

    val expenseColors = listOf(
        Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
        Color(0xFF03A694), Color(0xFF009688)
    )
    
    val incomeColors = listOf(
        Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
        Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
        Color(0xFFFF5722), Color(0xFF795548)
    )
    
    val colors = if (isExpense) expenseColors else incomeColors

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (totalAmount > 0) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var startAngle = -90f
                categoryTotals.values.forEachIndexed { index, amount ->
                    val sweepAngle = (amount / totalAmount).toFloat() * 360f
                    drawArc(
                        color = colors[index % colors.size],
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    startAngle += sweepAngle
                }
            }
        } else {
            // Empty state circle
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (isExpense) "Spent" else "Earned",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "$currencySymbol${String.format(Locale.getDefault(), "%.0f", totalAmount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SpendingBarChart(
    transactions: List<Transaction>,
    modifier: Modifier = Modifier
) {
    val expenses = transactions.filter { it.type == TransactionType.EXPENSE }
    
    val last7Days = (0..6).map { i ->
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -i)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.timeInMillis
    }.reversed()

    val dailyTotals = last7Days.map { day ->
        expenses.filter { 
            val itemCal = Calendar.getInstance()
            itemCal.timeInMillis = it.date
            itemCal.set(Calendar.HOUR_OF_DAY, 0)
            itemCal.set(Calendar.MINUTE, 0)
            itemCal.set(Calendar.SECOND, 0)
            itemCal.set(Calendar.MILLISECOND, 0)
            itemCal.timeInMillis == day 
        }.sumOf { it.amount }
    }

    val maxAmount = dailyTotals.maxOrNull()?.takeIf { it > 0 } ?: 1.0

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        dailyTotals.forEach { amount ->
            val barHeight = (amount / maxAmount).toFloat()
            Box(
                modifier = Modifier
                    .width(16.dp)
                    .fillMaxHeight(barHeight.coerceAtLeast(0.1f))
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}
