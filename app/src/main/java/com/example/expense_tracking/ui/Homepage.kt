package com.example.expense_tracking.ui

import android.R.attr.top
import androidx.annotation.ColorRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expense_tracking.R
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.data.Constants.CategoryConstants
import com.example.expense_tracking.data.model.Expense
import com.example.expense_tracking.viewmodel.HomeViewModel
import java.util.Calendar

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

    val totalBalance by viewModel.totalBalance.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val thisMonthExpenses by viewModel.thisMonthExpenses.collectAsState()
    val recentExpenses by viewModel.recentExpenses.collectAsState()

    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf("Home") }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" }
                )
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home_main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home_main") {
                HomeMainContent(
                    totalBalance = totalBalance,
                    currency = currency,
                    thisMonthExpenses = thisMonthExpenses,
                    recentExpenses = recentExpenses,
                    onViewAllClick = { navController.navigate("all_transactions") },
                    viewModel = viewModel
                )
            }
            composable("all_transactions") {
                AllTransactionPage(
                    allTransactionList = viewModel.allExpenses.collectAsState().value,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun HomeMainContent(
    totalBalance: Double,
    thisMonthExpenses: Double,
    currency: String,
    recentExpenses: List<Expense>,
    viewModel: HomeViewModel,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.welcome_back),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.homepage_intro),
            color = Color(0xFF424242)
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Homepage_totalBalance(amount = totalBalance.toInt(), currency = currency, modifier = Modifier.weight(1f).height(85.dp))
            Homepage_thisMonth(amount = thisMonthExpenses.toInt(), currency = currency, modifier = Modifier.weight(1f).height(85.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))

        Homepage_MonthlyTrends(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            viewModel = viewModel,
            allExpenses = viewModel.allExpenses.collectAsState().value
        )
        Spacer(modifier = Modifier.height(8.dp))

        Homepage_ExpenseSummary(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            allExpenses = viewModel.allExpenses.collectAsState().value
        )
        Spacer(modifier = Modifier.height(8.dp))

        Homepage_SpendingPredictions(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(8.dp))

        Homepage_RecentTransactions(
            recentExpenses = recentExpenses,
            onViewAllClick = onViewAllClick,
            modifier = Modifier.fillMaxWidth()
        )
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
                Image(
                    painter = painterResource(id = R.drawable.attach_money_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = stringResource(R.string.totalBalance)
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row {
                Text(
                    text = currency,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.padding(1.dp))
                Text(
                    text = amount.toString(),
                    fontSize = 24.sp
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
                Icon(
                    painter = painterResource(id = R.drawable.wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = null,
                    tint = Color(0xFF2196F3)
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = stringResource(R.string.thisMonth)
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row {
                Text(text = currency,
                    fontSize = 24.sp)
                Spacer(modifier = Modifier.padding(1.dp))
                Text(text = amount.toString(),
                    fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun Homepage_MonthlyTrends(
    viewModel: HomeViewModel,
    allExpenses: List<Expense>,
    modifier: Modifier = Modifier
) {

    var selectedMonth by remember { mutableStateOf("All Month") }
    var selectedYear by remember { mutableStateOf("All Year") }

    val monthMap = mapOf(
        "All Month" to "All Month",
        "01" to "Jan","02" to "Feb","03" to "Mar","04" to "Apr",
        "05" to "May","06" to "Jun","07" to "Jul","08" to "Aug",
        "09" to "Sep","10" to "Oct","11" to "Nov","12" to "Dec"
    )

    val months = monthMap.keys.toList()

    val years = listOf("All Year") +
            allExpenses.mapNotNull {
                it.date?.split("-")?.getOrNull(0)
            }.distinct()

    val isMonthlyView = selectedMonth == "All Month"

    // ---------------- FILTER ----------------

    val filteredExpenses = allExpenses.filter { expense ->

        val parts = expense.date?.split("-") ?: return@filter false

        val year = parts.getOrNull(0) ?: ""
        val month = parts.getOrNull(1)?.padStart(2,'0') ?: ""

        (selectedMonth == "All Month" || month == selectedMonth) &&
                (selectedYear == "All Year" || year == selectedYear)
    }

    // ---------------- DAILY TOTALS ----------------

    val dailyTotals = remember(filteredExpenses) {

        val totals = MutableList(31) { 0.0 }

        filteredExpenses.forEach {

            val day = it.date
                ?.split("-")
                ?.getOrNull(2)
                ?.toIntOrNull()

            if (day != null && day in 1..31) {
                totals[day - 1] += it.price ?: 0.0
            }
        }

        totals
    }

    // ---------------- MONTHLY TOTALS ----------------

    val monthlyTotals = remember(filteredExpenses) {

        val totals = MutableList(12) { 0.0 }

        filteredExpenses.forEach {

            val month = it.date
                ?.split("-")
                ?.getOrNull(1)
                ?.toIntOrNull()

            if (month != null && month in 1..12) {
                totals[month - 1] += it.price ?: 0.0
            }
        }

        totals
    }

    val chartData =
        if (isMonthlyView) monthlyTotals
        else dailyTotals

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text =
                        if (isMonthlyView)
                            "Monthly Spending Trend"
                        else
                            "Daily Spending Trend",
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    var monthExpanded by remember { mutableStateOf(false) }

                    Surface(
                        modifier = Modifier
                            .clickable { monthExpanded = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Text(
                                monthMap[selectedMonth] ?: selectedMonth,
                                fontSize = 14.sp
                            )

                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
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

                    var yearExpanded by remember { mutableStateOf(false) }

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

                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
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

            Spacer(modifier = Modifier.height(12.dp))

            MonthlyTrendsChart(
                expenses = chartData,
                isMonthly = isMonthlyView,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )
        }
    }
}

@Composable
fun MonthlyTrendsChart(
    expenses: List<Double>,
    isMonthly: Boolean,
    modifier: Modifier = Modifier
) {

    if (expenses.isEmpty()) {

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

        val n = expenses.size
        if (n == 2) return@Canvas

        val maxExpense = expenses.maxOrNull() ?: 0.0
        val minExpense = expenses.minOrNull() ?: 0.0

        val range =
            if (maxExpense - minExpense == 0.0) 1.0
            else maxExpense - minExpense

        val leftPadding = 50f
        val bottomPadding = 40f

        val usableWidth = canvasWidth - leftPadding
        val usableHeight = canvasHeight - bottomPadding

        val spacingX = usableWidth / (n - 1)

        val points = expenses.mapIndexed { index, value ->

            val x = leftPadding + spacingX * index

            val y =
                usableHeight -
                        ((value - minExpense) / range).toFloat() * usableHeight

            Offset(x, y)
        }

        val ySteps = 5

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

        points.forEach {

            drawCircle(
                color = Color(0xFF1565C0),
                radius = 6f,
                center = it
            )
        }

        val textPaint = android.graphics.Paint().apply {

            color = android.graphics.Color.BLACK
            textSize = 28f
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        for (i in 0..ySteps) {

            val yValue =
                minExpense + (range / ySteps * i)

            val yPos =
                usableHeight - i * (usableHeight / ySteps)

            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.0f", yValue),
                leftPadding - 10f,
                yPos + 8f,
                textPaint
            )
        }

        val monthLabels = listOf(
            "Jan","Feb","Mar","Apr","May","Jun",
            "Jul","Aug","Sep","Oct","Nov","Dec"
        )

        val xTextPaint = android.graphics.Paint().apply {

            color = android.graphics.Color.BLACK
            textSize = 24f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val step = maxOf(1, n / 10)

        for (i in 0 until n step step) {

            val xPos = leftPadding + spacingX * i

            val label =
                if (isMonthly) monthLabels[i]
                else (i + 1).toString()

            drawContext.canvas.nativeCanvas.drawText(
                label,
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
    allExpenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    var selectedMonth by remember { mutableStateOf("All Month") }
    var selectedYear by remember { mutableStateOf("All Year") }

    val monthMap = mapOf(
        "All Month" to "All Month",
        "01" to "Jan", "02" to "Feb", "03" to "Mar", "04" to "Apr",
        "05" to "May", "06" to "Jun", "07" to "Jul", "08" to "Aug",
        "09" to "Sep", "10" to "Oct", "11" to "Nov", "12" to "Dec"
    )
    val months = monthMap.keys.toList()
    val years = listOf("All Year") + allExpenses.mapNotNull { it.date?.split("-")?.getOrNull(0) }.distinct()

    // 过滤数据
    val filteredExpenses = allExpenses.filter { expense ->
        val expenseMonth = expense.date?.split("-")?.getOrNull(1)?.padStart(2,'0') ?: "01"
        val expenseYear = expense.date?.split("-")?.getOrNull(0) ?: "2000"

        (selectedMonth == "All Month" || expenseMonth == selectedMonth) &&
                (selectedYear == "All Year" || expenseYear == selectedYear)
    }

    val categoryExpenses = filteredExpenses.groupBy { it.category ?: "Others" }
        .mapValues { (_, expenses) -> expenses.sumOf { it.price ?: 0.0 } }

    val currency by viewModel.currency.collectAsState()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 标题 + 筛选在同一行
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Title
                Text(
                    text = stringResource(R.string.expenseSummary),
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold
                )

                // 筛选按钮
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Month Dropdown
                    var monthExpanded by remember { mutableStateOf(false) }
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
                    DropdownMenu(expanded = monthExpanded, onDismissRequest = { monthExpanded = false }) {
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

                    // Year Dropdown
                    var yearExpanded by remember { mutableStateOf(false) }
                    Surface(
                        modifier = Modifier
                            .clickable { yearExpanded = true }
                            .border(1.dp, Color.Gray, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF5F5F5)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedYear, fontSize = 14.sp) // 直接显示年份
                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(18.dp))
                        }
                    }
                    DropdownMenu(expanded = yearExpanded, onDismissRequest = { yearExpanded = false }) {
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

            Spacer(modifier = Modifier.height(12.dp))

            // 饼图 / ExpenseSummary 保留在 Column 下方
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
    modifier: Modifier = Modifier
) {

    if (expensesByCategory.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
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
        Color(0xFFBBDEFB),
        Color(0xFF2E7D32),
        Color(0xFF43A047),
        Color(0xFF66BB6A),
        Color(0xFFF9A825),
        Color(0xFFFFB300),
        Color(0xFFE65100),
        Color(0xFFFB8C00),
        Color(0xFF6A1B9A),
        Color(0xFF8E24AA),
        Color(0xFF00897B)
    )

    val total = expensesByCategory.values.sum()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        // ---------- PIE CHART ----------
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(180.dp)) {

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
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ---------- CATEGORY LIST ----------
        expensesByCategory.entries.forEachIndexed { index, entry ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Color indicator
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(
                            colors[index % colors.size],
                            shape = CircleShape
                        )
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Category name
                Text(
                    text = entry.key,
                    modifier = Modifier.weight(1f)
                )

                // Amount
                Text("$currency ${entry.value}")
            }
        }
    }
}

@Composable
fun Homepage_SpendingPredictions(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {

    val chartData by viewModel.predictionChartData.collectAsState()
    val min by viewModel.predictionMin.collectAsState()
    val max by viewModel.predictionMax.collectAsState()
    val prediction by viewModel.predictedNextMonth.collectAsState()

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Spending Prediction",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            if (chartData.isEmpty()) {
                Text("Not enough data for prediction")
                return@Card
            }
            Spacer(modifier = Modifier.height(12.dp))

            PredictionBarChart(
                data = chartData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Predicted next month spending",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "RM ${prediction.toInt()}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Based on your last 3 months spending trend",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Prediction range",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "RM ${min.toInt()} — RM ${max.toInt()}",
                color = Color.Gray
            )
        }

    }
}

@Composable
fun PredictionBarChart(
    data: List<Pair<String, Double>>,
    modifier: Modifier = Modifier
) {

    if (data.isEmpty()) return

    val maxValue = data.maxOf { it.second }
    val ySteps = 5

    Canvas(modifier = modifier.padding(16.dp)) {

        val canvasWidth = size.width
        val canvasHeight = size.height

        val leftPadding = 80f
        val bottomPadding = 80f
        val topPadding = 40f

        val usableWidth = canvasWidth - leftPadding
        val usableHeight = canvasHeight - bottomPadding - topPadding

        val barWidth = usableWidth / (data.size * 2)
        val spacing = barWidth

        val textPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 32f
            textAlign = android.graphics.Paint.Align.RIGHT
        }

        val xTextPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val predictPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.RED
            textSize = 28f
            textAlign = android.graphics.Paint.Align.CENTER
        }

        // Y Axis grid
        for (i in 0..ySteps) {

            val yValue = maxValue / ySteps * i

            val y =
                canvasHeight - bottomPadding -
                        (usableHeight / ySteps * i)

            drawLine(
                color = Color.LightGray,
                start = Offset(leftPadding, y),
                end = Offset(canvasWidth, y),
                strokeWidth = 2f
            )

            drawContext.canvas.nativeCanvas.drawText(
                yValue.toInt().toString(),
                leftPadding - 20f,
                y + 10f,
                textPaint
            )
        }

        data.forEachIndexed { index, item ->

            val barHeight =
                (item.second / maxValue * usableHeight).toFloat()

            val x =
                leftPadding + spacing +
                        index * (barWidth + spacing)

            val y =
                canvasHeight - bottomPadding - barHeight

            val isPrediction = index == data.lastIndex

            drawRect(
                color =
                    if (isPrediction)
                        Color(0xFFFF7043)
                    else
                        Color(0xFF1565C0),

                topLeft = Offset(x, y),

                size = androidx.compose.ui.geometry.Size(
                    barWidth,
                    barHeight
                )
            )

            // X label
            drawContext.canvas.nativeCanvas.drawText(
                item.first,
                x + barWidth / 2,
                canvasHeight - 30f,
                xTextPaint
            )

            // Prediction label
            if (isPrediction) {

                drawContext.canvas.nativeCanvas.drawText(
                    "Predict",
                    x + barWidth / 2,
                    y - 20f,
                    predictPaint
                )

                drawContext.canvas.nativeCanvas.drawText(
                    item.second.toInt().toString(),
                    x + barWidth / 2,
                    y - 50f,
                    predictPaint
                )
            }
        }
    }
}

@Composable
fun Homepage_RecentTransactions(
    recentExpenses: List<Expense>,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(R.string.recentTransaction),
                    modifier = modifier.weight(1f).padding(12.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onViewAllClick) {
                    Text("View All")
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            recentExpenses.forEach { expense ->
                Homepage_RecentTransaction_Record(
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
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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


@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ExpenseTrackingApplicationTheme {
        HomePage(userId = "preview_user")
    }
}