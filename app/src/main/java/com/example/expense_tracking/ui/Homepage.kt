package com.example.expense_tracking.ui

import android.R.attr.top
import androidx.annotation.ColorRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expense_tracking.R
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.data.Constants.CategoryConstants
import com.example.expense_tracking.data.model.Expense
import com.example.expense_tracking.viewmodel.HomeViewModel
import java.nio.file.Files.size
import java.util.Map.entry


@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    userId: String,
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
        viewModel.loadDailyExpenses(userId)
        viewModel.loadMonthlyCategoryExpenses(userId)
    }
    val accounts by viewModel.accounts.collectAsState()
    val totalBalance by viewModel.totalBalance.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val thisMonthExpenses by viewModel.thisMonthExpenses.collectAsState()
    val recentExpenses by viewModel.recentExpenses.collectAsState()

    var selectedTab by remember { mutableStateOf("Home") }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ){
                NavigationBarItem(
                    icon = {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Add"
                        )
                    },
                    label = { Text("Add") },
                    selected = selectedTab == "Add",
                    onClick = { selectedTab = "Add" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Scan"
                        )
                    },
                    label = { Text("Scan") },
                    selected = selectedTab == "Scan",
                    onClick = { selectedTab = "Scan" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Budget"
                        )
                    },
                    label = { Text("Budget") },
                    selected = selectedTab == "Budget",
                    onClick = { selectedTab = "Budget" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    selected = selectedTab == "Profile",
                    onClick = { selectedTab = "Profile" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
                    )
                )
            }
        }
    ) { innerPadding ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text( //Welcome, ${viewModel.currentUsername ?: "User"}
                text = stringResource(R.string.welcome_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.homepage_intro),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Homepage_totalBalance(
                    amount = totalBalance.toInt(),
                    currency = currency,
                    modifier = Modifier
                        .weight(1f)
                        .height(85.dp)
                )
                Homepage_thisMonth(
                    amount = thisMonthExpenses.toInt(),
                    currency = currency,
                    modifier = Modifier
                        .weight(1f)
                        .height(85.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_MonthlyTrends(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_ExpenseSummary(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    //。height(220.dp)
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(8.dp))

            Homepage_SpendingPredictions(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_RecentTransactions(
                recentExpenses = recentExpenses,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Homepage_totalBalance(
    modifier: Modifier = Modifier,
    currency: String,
    amount: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1565C0),
            contentColor = Color.White
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.totalBalance)
                )
            }
            Row {
                Text(
                    text = currency
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Text(
                    text = amount.toString()
                )
            }
        }
    }
}

@Composable
fun Homepage_thisMonth(
    modifier: Modifier = Modifier,
    currency: String,
    amount: Int
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.thisMonth)
                )
            }
            Row {
                Text(text = currency)
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text = amount.toString())
            }
        }
    }
}

@Composable
fun Homepage_MonthlyTrends(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val dailyExpense by viewModel.dailyExpenses.collectAsState()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Text(
                text = stringResource(R.string.monthlyTrend),
                modifier = Modifier.padding(10.dp)
            )
            MonthlyTrendsChart(
                expensesByDay = dailyExpense,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun MonthlyTrendsChart(
    expensesByDay: List<Double>,
    modifier: Modifier = Modifier
) {
    if (expensesByDay.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("No data yet", color = Color.Gray)
        }
        return
    }

    Canvas(modifier = modifier.padding(8.dp)) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val n = expensesByDay.size
        if (n < 2) return@Canvas

        val maxExpense = expensesByDay.maxOrNull() ?: 0.0
        val minExpense = expensesByDay.minOrNull() ?: 0.0

        val leftPadding = 50f
        val bottomPadding = 40f
        val usableWidth = canvasWidth - leftPadding
        val usableHeight = canvasHeight - bottomPadding
        val spacingX = usableWidth / (n - 1)

        val points = expensesByDay.mapIndexed { index, value ->
            val x = leftPadding + spacingX * index
            val y = usableHeight - ((value - minExpense) / (maxExpense - minExpense)).toFloat() * usableHeight
            Offset(x, y)
        }

        val ySteps = 5
        val gridPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.LTGRAY
            strokeWidth = 1f
        }
        for (i in 0..ySteps) {
            val yPos = usableHeight - i * (usableHeight / ySteps)
            drawLine(
                color = Color.LightGray,
                start = Offset(leftPadding, yPos),
                end = Offset(canvasWidth, yPos),
                strokeWidth = 1f
            )
        }

        for (i in 0 until points.lastIndex) {
            drawLine(
                color = Color(0xFF1565C0),
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4f
            )
        }

        points.forEach { point ->
            drawCircle(
                color = Color(0xFF1565C0),
                radius = 6f,
                center = point
            )
        }

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 28f
            textAlign = android.graphics.Paint.Align.RIGHT
        }
        for (i in 0..ySteps) {
            val yValue = minExpense + (maxExpense - minExpense) / ySteps * i
            val yPos = usableHeight - i * (usableHeight / ySteps)
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.0f", yValue),
                leftPadding - 10f, // 左边留空
                yPos + 8f,
                textPaint
            )
        }

        val xTextPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 24f
            textAlign = android.graphics.Paint.Align.CENTER
        }
        for (i in 0 until n step 2) {  // 每两天显示一次
            val xPos = leftPadding + spacingX * i
            drawContext.canvas.nativeCanvas.drawText(
                (i + 1).toString(),
                xPos,
                usableHeight + 30f,
                xTextPaint
            )
        }
    }
}

@Composable
fun Homepage_ExpenseSummary(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val categoryExpenses by viewModel.monthlyCategoryExpenses.collectAsState()
    val currency by viewModel.currency.collectAsState()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.expenseSummary),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            //
            if (categoryExpenses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No data yet", color = Color.Gray)
                }
            } else {
                ExpenseSummary(
                    expensesByCategory = categoryExpenses,
                    currency = currency,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ExpenseSummary(
    expensesByCategory: Map<String, Double>,
    currency: String,
    modifier: Modifier = Modifier) {
    if (expensesByCategory.isEmpty()) {
        Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("No data yet", color = Color.Gray)
        }
        return
    }

    val colors = listOf(
        Color(0xFF1565C0),
        Color(0xFF1E88E5),
        Color(0xFF2196F3),
        Color(0xFF64B5F6),
        Color(0xFF90CAF9),
        Color(0xFFBBDEFB)
    )

    val total = expensesByCategory.values.sum()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            var startAngle = -90f
            expensesByCategory.entries.forEachIndexed { index, entry ->
                val sweep = (entry.value / total * 360).toFloat()
                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = true
                )
                startAngle += sweep
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            expensesByCategory.entries.forEachIndexed { index, entry ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(colors[index % colors.size], shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${entry.key}:" )
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Text("${currency}")
                    Spacer(
                        modifier = Modifier.width(1.dp)
                    )
                    Text(" ${entry.value}")
                }
            }
        }
    }
}



@Composable
fun Homepage_SpendingPredictions(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.spendingPrediction),
            modifier = modifier.padding(10.dp)
        )
    }
}

@Composable
fun Homepage_RecentTransactions(
    recentExpenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.recentTransaction),
                modifier = modifier.padding(10.dp)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            recentExpenses.forEach { expense ->
                Homepage_RecentTransaction_Record(
                    currency = "MYR",
                    total_cost = expense.price ?: 0.0,
                    date = expense.date ?: "N/A",
                    description = expense.description ?: "N/A",
                    category = expense.category ?: "N/A"
                )
            }
        }
    }
}

@Composable
fun Homepage_RecentTransaction_Record(
    modifier: Modifier = Modifier,
    currency: String,
    total_cost: Double,
    date: String,
    description: String,
    category: String? = null
) {
    val iconColor = Color(0xFF64B5F6)

    val iconRes = if (category != null && CategoryConstants.CATEGORY_ICONS.containsKey(category)) {
        CategoryConstants.CATEGORY_ICONS[category]!!
    } else {
        CategoryConstants.CATEGORY_ICONS[CategoryConstants.OTHERS]!!
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
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
                Text(
                    text = description
                )
                Text(
                    text = date,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "-",
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = currency,
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = total_cost.toString(),
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ExpenseTrackingApplicationTheme {
        HomePage(userId = "preview_user")
    }
}