package com.example.expense_tracking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking.data.Constants.CategoryConstants
import com.example.expense_tracking.data.model.Expense
import com.example.expense_tracking.viewmodel.AddExpenseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.collectAsState

@Composable
fun AllTransactionPage(
    allTransactionList: List<Expense>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All Category") }
    var selectedMonth by remember { mutableStateOf("All Month") }
    var selectedYear by remember { mutableStateOf("All Year") }

    val viewModel: AddExpenseViewModel = viewModel()
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val userCategories by viewModel.userCategories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserCategories(
            currentUserId,
            FirebaseFirestore.getInstance()
        )
    }

    val categories =
        remember(userCategories) {

            buildList {

                add("All Category")

                addAll(CategoryConstants.CATEGORIES)

                userCategories.forEach {
                    if (!contains(it)) add(it)
                }
            }
        }

    val monthMap = mapOf(
        "All Month" to "All Month",
        "01" to "Jan", "02" to "Feb", "03" to "Mar", "04" to "Apr",
        "05" to "May", "06" to "Jun", "07" to "Jul", "08" to "Aug",
        "09" to "Sep", "10" to "Oct", "11" to "Nov", "12" to "Dec"
    )
    val months = monthMap.keys.toList()

    val years = listOf("All Year") + allTransactionList.mapNotNull { it.date?.split("-")?.getOrNull(0) }.distinct()

    val filteredTransactions = allTransactionList.filter { expense ->
        val matchesCategory = selectedCategory == "All Category" ||
                expense.category?.trim()?.lowercase() == selectedCategory.trim().lowercase()

        val expenseMonth = expense.date?.split("-")?.getOrNull(1)?.padStart(2,'0') ?: "01"
        val matchesMonth = selectedMonth == "All Month" || expenseMonth == selectedMonth

        val expenseYear = expense.date?.split("-")?.getOrNull(0) ?: "2000"
        val matchesYear = selectedYear == "All Year" || expenseYear == selectedYear

        matchesCategory && matchesMonth && matchesYear
    }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        topBar = { TransactionTopBar(onBackClick = onBackClick) },
        containerColor = backgroundColor
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var categoryExpanded by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        modifier = Modifier
                            .clickable { categoryExpanded = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedCategory, fontSize = 14.sp)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }

                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                var monthExpanded by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        modifier = Modifier
                            .clickable { monthExpanded = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(monthMap[selectedMonth] ?: selectedMonth, fontSize = 14.sp)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = monthExpanded,
                        onDismissRequest = { monthExpanded = false }
                    ) {
                        months.forEach { month ->
                            DropdownMenuItem(
                                text = { Text(monthMap[month] ?: month) },
                                onClick = {
                                    selectedMonth = month
                                    monthExpanded = false
                                }
                            )
                        }
                    }
                }

                var yearExpanded by remember { mutableStateOf(false) }
                Box {
                    Surface(
                        modifier = Modifier
                            .clickable { yearExpanded = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedYear, fontSize = 14.sp)
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = yearExpanded,
                        onDismissRequest = { yearExpanded = false }
                    ) {
                        years.forEach { year ->
                            DropdownMenuItem(
                                text = { Text(year) },
                                onClick = {
                                    selectedYear = year
                                    yearExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
            ) {
                items(filteredTransactions) { expense ->
                    TransactionCard(
                        currency = "RM",
                        total_cost = expense.price ?: 0.0,
                        date = expense.date ?: "N/A",
                        description = expense.description ?: "N/A",
                        category = expense.category ?: "N/A"
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionTopBar(onBackClick: () -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            modifier = Modifier
                .size(28.dp)
                .clickable { onBackClick() }
        )
        Spacer(
            modifier = Modifier.width(8.dp)
        )
        Text(
            text = "All Transactions",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TransactionCard(
    currency: String,
    total_cost: Double,
    date: String,
    description: String,
    category: String? = null,
    modifier: Modifier = Modifier
) {
    val iconColor = Color(0xFF64B5F6)
    val iconRes = when {
        category == null -> CategoryConstants.CATEGORY_ICONS[CategoryConstants.OTHERS]!!
        CategoryConstants.USER_CATEGORY_ICONS.containsKey(category) ->
            CategoryConstants.CATEGORY_ICONS[CategoryConstants.USER_CATEGORY_ICONS[category]]!!
        CategoryConstants.CATEGORY_ICONS.containsKey(category) ->
            CategoryConstants.CATEGORY_ICONS[category]!!
        else -> CategoryConstants.CATEGORY_ICONS[CategoryConstants.OTHERS]!!
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = category ?: "Others",
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = description)
                Text(text = date, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = currency)
                Spacer(modifier = Modifier.width(2.dp))
                Text(text = total_cost.toString())
            }
        }
    }
}