package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.data.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class HomeRepository {

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

    suspend fun getUserThisMonthExpensesForAllAccounts(userId: String): Double {
        return try {
            val cal = Calendar.getInstance()
            val yearMonth = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)

            val snapshot = firestore.collection("expenses")
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
                .filter { it.date?.startsWith(yearMonth) == true }
                .sumOf { it.price ?: 0.0 }
        } catch (e: Exception) {
            e.printStackTrace()
            0.0
        }
    }

    suspend fun getUserRecentExpensesForAllAccounts(userId: String, limit: Int = 4): List<Expense> {
        return try {
            val snapshot = firestore.collection("expenses")
                .whereEqualTo("user_id", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}
