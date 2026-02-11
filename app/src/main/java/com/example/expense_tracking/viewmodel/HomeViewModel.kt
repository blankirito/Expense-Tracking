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
                    _thisMonthExpenses.value = expenses
                        .filter { it.date?.startsWith(yearMonth) == true }
                        .sumOf { it.price ?: 0.0 }
                    _recentExpenses.value = expenses.sortedByDescending { it.date }.take(4)
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


}
