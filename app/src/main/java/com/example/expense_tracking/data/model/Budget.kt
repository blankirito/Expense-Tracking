package com.example.expense_tracking.data.model

data class Budget(
    val id: String = "",
    val user_id: String = "",
    val account_id: String = "",
    val category: String = "",
    val limit: Double = 0.0,
    val spend: Double = 0.0,
)
