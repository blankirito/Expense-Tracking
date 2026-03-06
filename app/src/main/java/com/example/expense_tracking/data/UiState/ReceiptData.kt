package com.example.expense_tracking.data.UiState

data class ReceiptData(
    val reference: String = "",
    val amount: String = "",
    val date: String = "",
    val account: String = "Bank",
    val category: String = "",
    val description: String = ""
)

