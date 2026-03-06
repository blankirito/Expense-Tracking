package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.Repository.AddRepository
import kotlinx.coroutines.launch
import com.example.expense_tracking.data.Constants.*
import com.google.firebase.firestore.FirebaseFirestore

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

    fun onAccountSelected(accountId: String) { selectedAccountId = accountId }
    fun onAmountChange(value: String) { amount = value }
    fun onCategoryChange(value: String) { category = value }
    fun onDateChange(value: String) { date = value }
    fun onDescriptionChange(value: String) { description = value }

    fun saveExpense(userId: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
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

                repository.addExpenseAndSyncAll(expense)
                onSuccess()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun loadUserCategories(userId: String, firestore: FirebaseFirestore) {
        firestore.collection("categories")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot.documents) {
                    val name = doc.getString("name") ?: continue
                    val iconKey = doc.getString("icon") ?: CategoryConstants.OTHERS

                    if (!CategoryConstants.CATEGORIES.contains(name)) {
                        CategoryConstants.CATEGORIES.add(name)
                        CategoryConstants.USER_CATEGORY_ICONS[name] = iconKey
                    }
                }
            }
    }
}