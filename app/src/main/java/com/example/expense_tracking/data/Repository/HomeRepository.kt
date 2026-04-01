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

    suspend fun getUserThisMonthExpensesForAllAccounts(userId: String): Double {
        val cal = Calendar.getInstance()
        val currentMonth = cal.get(Calendar.MONTH)
        val currentYear = cal.get(Calendar.YEAR)

        val snapshot = firestore.collection("expenses")
            .whereEqualTo("user_id", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
            .filter { expense ->
                expense.date?.let {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val expenseDate = try { sdf.parse(it) } catch(e: Exception) { null }
                    expenseDate?.let { d ->
                        val expCal = Calendar.getInstance().apply { time = d }
                        expCal.get(Calendar.MONTH) == currentMonth &&
                                expCal.get(Calendar.YEAR) == currentYear
                    } ?: false
                } ?: false
            }
            .sumOf { it.price ?: 0.0 }
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

    suspend fun getUserThisMonthExpensesForAllAccountsDetailed(userId: String): List<Expense> {
        val cal = Calendar.getInstance()
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)

        val snapshot = firestore.collection("expenses")
            .whereEqualTo("user_id", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
            .filter { it.date?.startsWith(yearMonth) == true }
    }

    suspend fun getUserThisMonthExpensesByCategory(userId: String): Map<String, Double> {
        val cal = Calendar.getInstance()
        val yearMonth = SimpleDateFormat("yyyy-MM", Locale.US).format(cal.time)

        val snapshot = firestore.collection("expenses")
            .whereEqualTo("user_id", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(Expense::class.java) }
            .filter { it.date?.startsWith(yearMonth) == true }
            .groupBy { it.category ?: "Others" }
            .mapValues { (_, expenses) -> expenses.sumOf { it.price ?: 0.0 } }
    }

    suspend fun getUserAllExpenses(userId: String): List<Expense> {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("expenses")
            .whereEqualTo("user_id", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(Expense::class.java)
        }
    }
}
