package com.example.expense_tracking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Constants.CategoryConstants
import com.example.expense_tracking.data.Repository.HomeRepository
import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(
    private val repository: HomeRepository = HomeRepository()
) : ViewModel() {

    // ---------------- STATE ----------------

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    private val _currency = MutableStateFlow("RM")
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

    // -------- Prediction --------

    private val _predictedNextMonth = MutableStateFlow(0.0)
    val predictedNextMonth: StateFlow<Double> = _predictedNextMonth

    private val _predictionChartData =
        MutableStateFlow<List<Pair<String, Double>>>(emptyList())
    val predictionChartData: StateFlow<List<Pair<String, Double>>> = _predictionChartData

    private val _predictionMin = MutableStateFlow(0.0)
    val predictionMin: StateFlow<Double> = _predictionMin

    private val _predictionMax = MutableStateFlow(0.0)
    val predictionMax: StateFlow<Double> = _predictionMax

    private val _predictionConfidence =
        MutableStateFlow(0)

    val predictionConfidence =
        _predictionConfidence.asStateFlow()

    private val _predictionTrend =
        MutableStateFlow("Stable")

    val predictionTrend =
        _predictionTrend.asStateFlow()

    // ---------------- FIRESTORE ----------------

    private val firestore = FirebaseFirestore.getInstance()

    private var accountListener: ListenerRegistration? = null
    private var expenseListener: ListenerRegistration? = null

    private var currentUserId: String = ""

    // ---------------- USER LOAD ----------------

    fun loadUserData(userId: String) {

        if (currentUserId == userId) return

        resetState()

        currentUserId = userId

        viewModelScope.launch {

            loadUserCategories(userId)

            val accounts = repository.getUserAccounts(userId)
            _accounts.value = accounts
            _totalBalance.value = accounts.sumOf { it.current_balance ?: 0.0 }

            _currencies.value = repository.getUserAccountCurrency(userId)
            _currency.value = _currencies.value.firstOrNull() ?: "MYR"

            val recent = repository.getUserRecentExpensesForAllAccounts(userId)
            _recentExpenses.value = recent

            val all = repository.getUserAllExpenses(userId)

            val sorted = all.sortedByDescending { it.date }

            _allExpenses.value = sorted

            calculateThisMonthExpenses(all)

            loadDailyExpenses(userId)
            loadMonthlyCategoryExpenses(userId)
        }

        listenAccountChanges(userId)
        listenExpenseChanges(userId)
    }

    private suspend fun loadUserCategories(userId: String) {

        val snapshot = firestore.collection("categories")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        snapshot.documents.forEach {

            val name = it.getString("name") ?: return@forEach
            val icon = it.getString("icon") ?: return@forEach

            if (!CategoryConstants.CATEGORIES.contains(name)) {
                CategoryConstants.CATEGORIES.add(name)
            }

            CategoryConstants.USER_CATEGORY_ICONS[name] = icon
        }
    }

    // ---------------- RESET ----------------

    private fun resetState() {

        _accounts.value = emptyList()
        _currencies.value = emptyList()
        _currency.value = "MYR"

        _totalBalance.value = 0.0
        _thisMonthExpenses.value = 0.0

        _recentExpenses.value = emptyList()
        _allExpenses.value = emptyList()

        _dailyExpenses.value = emptyList()
        _monthlyCategoryExpenses.value = emptyMap()

        _predictionChartData.value = emptyList()
        _predictedNextMonth.value = 0.0
        _predictionMin.value = 0.0
        _predictionMax.value = 0.0
    }

    // ---------------- ACCOUNT LISTENER ----------------

    private fun listenAccountChanges(userId: String) {

        accountListener?.remove()

        accountListener =
            firestore.collection("accounts")
                .whereEqualTo("user_id", userId)
                .addSnapshotListener { snapshot, _ ->

                    if (snapshot != null) {

                        val accounts =
                            snapshot.documents.mapNotNull {
                                it.toObject(Account::class.java)
                            }

                        _accounts.value = accounts

                        _totalBalance.value =
                            accounts.sumOf { it.current_balance ?: 0.0 }
                    }
                }
    }

    // ---------------- EXPENSE LISTENER ----------------

    private fun listenExpenseChanges(userId: String) {

        expenseListener?.remove()

        expenseListener =
            firestore.collection("expenses")
                .whereEqualTo("user_id", userId)
                .addSnapshotListener { snapshot, _ ->

                    if (snapshot != null) {

                        val expenses =
                            snapshot.documents.mapNotNull {
                                it.toObject(Expense::class.java)
                            }

                        val sorted =
                            expenses.sortedByDescending { it.date }

                        _allExpenses.value = sorted

                        _recentExpenses.value = sorted.take(4)

                        calculateThisMonthExpenses(sorted)

                        calculateSpendingPrediction()
                    }
                }
    }

    // ---------------- THIS MONTH ----------------

    private fun calculateThisMonthExpenses(
        expenses: List<Expense>
    ) {

        val currentMonth =
            SimpleDateFormat("yyyy-MM", Locale.US)
                .format(Date())

        val total =
            expenses
                .filter {
                    it.date?.startsWith(currentMonth) == true
                }
                .sumOf { it.price ?: 0.0 }

        _thisMonthExpenses.value = total
    }

    // ---------------- DAILY EXPENSE ----------------

    fun loadDailyExpenses(userId: String) {

        viewModelScope.launch {

            val expenses =
                repository.getUserThisMonthExpensesForAllAccountsDetailed(userId)

            val cal = Calendar.getInstance()

            val days =
                cal.getActualMaximum(Calendar.DAY_OF_MONTH)

            val totals =
                MutableList(days) { 0.0 }

            expenses.forEach {

                val day =
                    it.date
                        ?.split("-")
                        ?.getOrNull(2)
                        ?.toIntOrNull()

                if (day != null && day in 1..days) {

                    totals[day - 1] += it.price ?: 0.0
                }
            }

            _dailyExpenses.value = totals
        }
    }

    // ---------------- CATEGORY EXPENSE ----------------

    fun loadMonthlyCategoryExpenses(userId: String) {

        viewModelScope.launch {

            _monthlyCategoryExpenses.value =
                repository.getUserThisMonthExpensesByCategory(userId)
        }
    }

    // ---------------- PREDICTION ----------------

    private fun calculateSpendingPrediction() {

        val expenses = _allExpenses.value

        if (expenses.isEmpty()) {

            _predictionChartData.value = emptyList()
            return
        }

        val monthlyTotals = mutableMapOf<String, Double>()

        expenses.forEach {

            val date = it.date ?: return@forEach

            val month = date.substring(0, 7)

            monthlyTotals[month] =
                (monthlyTotals[month] ?: 0.0) +
                        (it.price ?: 0.0)
        }

        val currentMonth =
            SimpleDateFormat("yyyy-MM", Locale.US)
                .format(Date())

        val filtered =
            monthlyTotals.filterKeys { it < currentMonth }

        val sorted =
            filtered.keys.sorted()

        val last3 =
            sorted.takeLast(3)

        val values =
            last3.mapIndexed { index, month ->
                Pair(index + 1, filtered[month] ?: 0.0)
            }

        if (values.size < 2) {

            _predictionChartData.value = emptyList()
            return
        }

        val n = values.size

        val sumX = values.sumOf { it.first }
        val sumY = values.sumOf { it.second }

        val sumXY =
            values.sumOf { it.first * it.second }

        val sumX2 =
            values.sumOf { it.first * it.first }

        val denominator =
            (n * sumX2 - sumX * sumX)

        val slope =
            if (denominator == 0)
                0.0
            else
                (n * sumXY - sumX * sumY) /
                        denominator

        val intercept =
            (sumY - slope * sumX) / n

        val prediction =
            intercept + slope * (n + 1)

        val safePrediction =
            if (prediction < 0) 0.0 else prediction

        _predictedNextMonth.value = safePrediction

        val chart = mutableListOf<Pair<String, Double>>()

        last3.forEach {

            chart.add(
                Pair(
                    it.substring(5),
                    filtered[it] ?: 0.0
                )
            )
        }

        chart.add(Pair("Predict", safePrediction))

        _predictionChartData.value = chart

        val margin = safePrediction * 0.1

        _predictionMin.value = safePrediction - margin
        _predictionMax.value = safePrediction + margin

    }

    // ---------------- CLEAR LISTENERS ----------------

    override fun onCleared() {

        super.onCleared()

        accountListener?.remove()
        expenseListener?.remove()
    }
}