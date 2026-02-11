package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Repository.HomeRepository
import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    private var currentUserId: String = ""

    fun loadUserData(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            val accounts = repository.getUserAccounts(userId)
            _accounts.value = accounts
            _totalBalance.value = accounts.sumOf { it.current_balance ?: 0.0 }

            _currencies.value = repository.getUserAccountCurrency(userId)
            _currency.value = _currencies.value.firstOrNull() ?: "MYR"
            _thisMonthExpenses.value = repository.getUserThisMonthExpensesForAllAccounts(userId)
            _recentExpenses.value = repository.getUserRecentExpensesForAllAccounts(userId)
        }
    }

}
