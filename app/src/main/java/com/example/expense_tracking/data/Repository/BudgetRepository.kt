package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Budget
import com.example.expense_tracking.data.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class BudgetRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getUserAccounts(userId: String): List<Account> {
        return try {
            val snapshot = firestore.collection("accounts")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Account::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUserAccountCurrency(userId: String): List<String> {
        return try {
            val snapshot = firestore.collection("accounts")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            snapshot.documents
                .mapNotNull { it.getString("currency") }
                .distinct()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUserTotalMonthlyBudgetSpend(userId: String): Double {
        return try {
            val snapshot = firestore.collection("budgets")
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                .sumOf { it.spend ?: 0.0 }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    suspend fun getUserTotalMonthlyBudgetLimit(userId: String): Double {
        return try {
            val snapshot = firestore.collection("budgets")
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
                .sumOf { it.limit ?: 0.0 }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    suspend fun getUserBudgets(userId: String): List<Budget> {
        return try {
            val snapshot = firestore.collection("budgets")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Budget::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateBudget(budget: Budget) {
        try {
            firestore.collection("budgets")
                .document(budget.id!!)
                .set(budget)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun addBudget(budget: Budget) {
        try {
            firestore.collection("budgets")
                .add(budget)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteBudget(budget: Budget) {
        try {
            firestore.collection("budgets")
                .document(budget.id!!)
                .delete()
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getUserExpenses(userId: String): List<Expense> {
        return try {
            val snapshot = firestore.collection("expenses")
                .whereEqualTo("user_id", userId)
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}