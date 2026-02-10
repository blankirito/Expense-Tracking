package com.example.expense_tracking.data.model

data class Account(
    val id: String = "",
    val user_id: String = "",
    val name: String = "",
    val type: String = "",
    val currency: String = "",
    val current_balance: Double = 0.0
)

