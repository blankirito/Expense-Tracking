package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AddRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun addExpenseDetail(expense: Expense) {
        firestore
            .collection("expenses")
            .document(expense.id)
            .set(expense)
            .await()
    }

    suspend fun updateAccountBalance(
        accountId: String,
        newBalance: Double
    ) {
        firestore
            .collection("accounts")
            .document(accountId)
            .update("current_balance", newBalance)
            .await()
    }

    suspend fun getAccount(accountId: String): Account {
        val snapshot = firestore
            .collection("accounts")
            .document(accountId)
            .get()
            .await()

        return snapshot.toObject(Account::class.java)
            ?: throw IllegalStateException("Account not found: $accountId")
    }

    fun createExpense(
        userId: String,
        accountId: String,
        category: String,
        price: Double,
        date: String,
        description: String,
        paymentMethod: String
    ): Expense {
        return Expense(
            id = java.util.UUID.randomUUID().toString(),
            user_id = userId,
            account_id = accountId,
            category = category,
            price = price,
            date = date,
            description = description,
            payment_method = paymentMethod
        )
    }

}