package com.example.expense_tracking.data.model

data class Expense(
    val id: String = "",
    val user_id: String = "",
    val account_id: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val date: String = "",
    val description: String = "",
    val payment_method: String = "",
    val receipt_image: String = ""
)
