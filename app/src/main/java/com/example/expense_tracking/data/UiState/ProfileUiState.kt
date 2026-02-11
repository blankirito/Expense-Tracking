package com.example.expense_tracking.data.UiState

import com.example.expense_tracking.data.model.Account

data class ProfileUiState(
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val currency: String = "",
    val cash: Double = 0.0,
    val bank: Double = 0.0,
    val ewallet: Double = 0.0,
    val accounts: List<Account> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
