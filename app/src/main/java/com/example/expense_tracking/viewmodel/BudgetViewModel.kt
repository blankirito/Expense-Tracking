package com.example.expense_tracking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Repository.BudgetRepository
import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Budget
import com.example.expense_tracking.data.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(private val repository: BudgetRepository = BudgetRepository()) : ViewModel() {

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts

    private val _currency = MutableStateFlow("")
    val currency: StateFlow<String> = _currency

    private val _currencies = MutableStateFlow<List<String>>(emptyList())

    private val _totalLimit = MutableStateFlow(0.0)
    val totalLimit: StateFlow<Double> = _totalLimit

    private val _totalSpend = MutableStateFlow(0.0)
    val totalSpend: StateFlow<Double> = _totalSpend

    private val _budgets = MutableStateFlow<List<Budget>>(emptyList())
    val budgets: StateFlow<List<Budget>> = _budgets

    val categorySpends = _budgets.map { budgets ->
        budgets.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.spend } }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    private var currentUserId: String = ""

    fun loadUserData(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            val accounts = repository.getUserAccounts(userId)
            _accounts.value = accounts
            _currencies.value = repository.getUserAccountCurrency(userId)
            _currency.value = _currencies.value.firstOrNull() ?: "RM"
            _budgets.value = repository.getUserBudgets(userId)
            _totalSpend.value = repository.getUserTotalMonthlyBudgetSpend(userId)
            _totalLimit.value = repository.getUserTotalMonthlyBudgetLimit(userId)
        }
    }

    fun updateCategory(oldName: String, newName: String, newLimit: Double) {
        viewModelScope.launch {
            val updatedBudgets = _budgets.value.map {
                if (it.category == oldName) it.copy(category = newName, limit = newLimit)
                else it
            }
            _budgets.value = updatedBudgets

            _totalLimit.value = updatedBudgets.sumOf { it.limit ?: 0.0 }

            updatedBudgets.find { it.category == newName }?.let { budget ->
                repository.updateBudget(budget)
            }
        }
    }

    fun addNewCategory(categoryName: String, limit: Double) {
        viewModelScope.launch {
            val newBudget = Budget(
                id = java.util.UUID.randomUUID().toString() ,
                user_id = currentUserId,
                category = categoryName,
                limit = limit,
                spend = 0.0
            )

            _budgets.value = _budgets.value + newBudget

            _totalLimit.value = _budgets.value.sumOf { it.limit ?: 0.0 }

            repository.addBudget(newBudget)
        }
    }

    fun deleteCategory(category: String) {
        viewModelScope.launch {
            val budgetToDelete = _budgets.value.find { it.category == category }

            budgetToDelete?.let {
                repository.deleteBudget(it)
            }

            _budgets.value = _budgets.value.filter { it.category != category }

            _totalLimit.value = _budgets.value.sumOf { it.limit ?: 0.0 }
        }
    }



}