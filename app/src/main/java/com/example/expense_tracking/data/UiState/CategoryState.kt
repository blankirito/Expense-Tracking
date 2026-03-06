package com.example.expense_tracking.data.UiState

interface CategoryState {
    var category: String
    fun onCategoryChange(newCategory: String)
}