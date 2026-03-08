package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Repository.HomeRepository
import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Expense
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class HomeViewModel(private val repository: HomeRepository = HomeRepository()) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    private val _currency = MutableStateFlow("")
    val currency: StateFlow<String> = _currency

    private val _currencies = MutableStateFlow<List<String>>(emptyList())
    val currencies: StateFlow<List<String>> = _currencies

    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance

    private val _thisMonthExpenses = MutableStateFlow(0.0)
    val thisMonthExpenses: StateFlow<Double> = _thisMonthExpenses

    private val _recentExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val recentExpenses: StateFlow<List<Expense>> = _recentExpenses

    private val _dailyExpenses = MutableStateFlow<List<Double>>(emptyList())
    val dailyExpenses: StateFlow<List<Double>> = _dailyExpenses

    private val _monthlyCategoryExpenses = MutableStateFlow<Map<String, Double>>(emptyMap())
    val monthlyCategoryExpenses: StateFlow<Map<String, Double>> = _monthlyCategoryExpenses

    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpenses: StateFlow<List<Expense>> = _allExpenses

    private val _predictedNextMonth = MutableStateFlow(0.0)
    val predictedNextMonth: StateFlow<Double> = _predictedNextMonth

    private val _predictionChartData =
        MutableStateFlow<List<Pair<String, Double>>>(emptyList())

    val predictionChartData: StateFlow<List<Pair<String, Double>>> =
        _predictionChartData

    private val _predictionMin = MutableStateFlow(0.0)
    val predictionMin: StateFlow<Double> = _predictionMin

    private val _predictionMax = MutableStateFlow(0.0)
    val predictionMax: StateFlow<Double> = _predictionMax

    private var currentUserId: String = ""

    fun loadUserData(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            val accounts = repository.getUserAccounts(userId)
            _accounts.value = accounts
            _totalBalance.value = accounts.sumOf { it.current_balance ?: 0.0 }

            _currencies.value = repository.getUserAccountCurrency(userId)
            _currency.value = _currencies.value.firstOrNull() ?: "MYR"

            refreshExpenses()
        }

        listenAccountChanges(userId)
        listenExpenseChanges(userId)
    }

    private fun refreshExpenses() {
        viewModelScope.launch {
            _thisMonthExpenses.value = repository.getUserThisMonthExpensesForAllAccounts(currentUserId)
            _recentExpenses.value = repository.getUserRecentExpensesForAllAccounts(currentUserId)
        }
    }

    private fun listenAccountChanges(userId: String) {
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        firestore.collection("accounts")
            .whereEqualTo("user_id", userId)
            .addSnapshotListener { snapshot, _ ->
                if(snapshot != null){
                    val accounts = snapshot.documents.mapNotNull { it.toObject(Account::class.java) }
                    _accounts.value = accounts
                    _totalBalance.value = accounts.sumOf { it.current_balance ?: 0.0 }
                }
            }
    }

    private fun listenExpenseChanges(userId: String) {
        val firestore = com.google.firebase.firestore.FirebaseFirestore.getInstance()
        val cal = java.util.Calendar.getInstance()
        val yearMonth = java.text.SimpleDateFormat("yyyy-MM", java.util.Locale.US).format(cal.time)

        firestore.collection("expenses")
            .whereEqualTo("user_id", userId)
            .addSnapshotListener { snapshot, _ ->
                if(snapshot != null){
                    val expenses = snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }

                    // 本月总支出
                    _thisMonthExpenses.value = expenses
                        .filter { it.date?.startsWith(yearMonth) == true }
                        .sumOf { it.price ?: 0.0 }

                    // Recent Transactions 只显示 4 条
                    _recentExpenses.value = expenses.sortedByDescending { it.date }.take(4)

                    // All Transactions 显示全部
                    _allExpenses.value = expenses.sortedByDescending { it.date }

                    calculateSpendingPrediction()
                }
            }
    }

    fun loadDailyExpenses(userId: String) {
        viewModelScope.launch {
            val expenses = repository.getUserThisMonthExpensesForAllAccountsDetailed(userId)

            val cal = Calendar.getInstance()
            val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            val dailyTotals = MutableList(daysInMonth) { 0.0 }

            expenses.forEach { expense ->
                expense.date?.let { dateStr ->
                    val day = dateStr.split("-")[2].toIntOrNull() ?: return@let
                    if (day in 1..daysInMonth) {
                        dailyTotals[day - 1] += expense.price ?: 0.0
                    }
                }
            }

            _dailyExpenses.value = dailyTotals
        }
    }

    fun loadMonthlyCategoryExpenses(userId: String) {
        viewModelScope.launch {
            _monthlyCategoryExpenses.value = repository.getUserThisMonthExpensesByCategory(userId)
        }
    }

    fun calculateSpendingPrediction() {

        val expenses = _allExpenses.value
        if (expenses.isEmpty()) return

        val monthlyTotals = mutableMapOf<String, Double>()

        expenses.forEach { expense ->
            val date = expense.date ?: return@forEach
            val month = date.substring(0, 7)

            monthlyTotals[month] =
                (monthlyTotals[month] ?: 0.0) + (expense.price ?: 0.0)
        }

        val currentMonth = java.text.SimpleDateFormat(
            "yyyy-MM",
            java.util.Locale.US
        ).format(java.util.Date())

        val filteredMonths =
            monthlyTotals.filterKeys { it != currentMonth }

        val sortedMonths =
            filteredMonths.keys.sorted()

        val last3Months =
            sortedMonths.takeLast(3)

        val values = last3Months.mapIndexed { index, month ->
            Pair(index + 1, filteredMonths[month] ?: 0.0)
        }

        if (values.size < 2) return

        val n = values.size
        val sumX = values.sumOf { it.first }
        val sumY = values.sumOf { it.second }
        val sumXY = values.sumOf { it.first * it.second }
        val sumX2 = values.sumOf { it.first * it.first }

        val denominator = (n * sumX2 - sumX * sumX)

        val slope =
            if (denominator.toDouble() == 0.0) 0.0
            else (n * sumXY - sumX * sumY) / denominator

        val intercept =
            (sumY - slope * sumX) / n

        val prediction =
            intercept + slope * (n + 1)

        // ⭐ 关键修复 (UI下面文字)
        _predictedNextMonth.value = prediction

        val chartData = mutableListOf<Pair<String, Double>>()

        last3Months.forEach {
            chartData.add(
                Pair(
                    it.substring(5),
                    filteredMonths[it] ?: 0.0
                )
            )
        }

        chartData.add(Pair("Predict", prediction))

        _predictionChartData.value = chartData

        val errorMargin = prediction * 0.1

        _predictionMin.value = prediction - errorMargin
        _predictionMax.value = prediction + errorMargin
    }

}
