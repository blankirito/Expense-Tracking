package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Repository.AddRepository
import com.example.expense_tracking.data.model.Expense
import kotlinx.coroutines.launch
import com.example.expense_tracking.data.Constants.*

class AddExpenseViewModel(
    private val repository: AddRepository = AddRepository()
) : ViewModel() {

    var selectedAccountId by mutableStateOf("")
        private set

    var currency by mutableStateOf("")
        private set

    var amount by mutableStateOf("")
        private set

    var category by mutableStateOf(CategoryConstants.CATEGORIES.first())
        private set

    var date by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    fun onAccountSelected(accountId: String) {
        selectedAccountId = accountId
    }

    fun onCurrencyChange(value: String) {
        currency = value
    }

    fun onAmountChange(value: String) {
        amount = value
    }

    fun onCategoryChange(value: String) {
        category = value
    }

    fun onDateChange(value: String) {
        date = value
    }

    fun onDescriptionChange(value: String) {
        description = value
    }

    fun saveExpense(
        userId: String,
        onSuccess: () -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (selectedAccountId.isBlank()) return@launch
                val expenseAmount = amount.toDoubleOrNull() ?: return@launch

                val account = repository.getAccount(selectedAccountId)

                val expense = repository.createExpense(
                    userId = userId,
                    accountId = selectedAccountId,
                    category = category,
                    price = expenseAmount,
                    date = date,
                    description = description,
                    paymentMethod = account.name
                )

                repository.addExpenseAndSyncAll(
                    expense = expense,
                )

                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
