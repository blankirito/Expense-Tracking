package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.Budget
import com.example.expense_tracking.data.model.Expense
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AddRepository {

    private val firestore = FirebaseFirestore.getInstance()

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

    suspend fun getAccount(accountId: String) =
        firestore.collection("accounts")
            .document(accountId)
            .get()
            .await()
            .toObject(com.example.expense_tracking.data.model.Account::class.java)
            ?: throw IllegalStateException("Account not found: $accountId")

    suspend fun addExpenseAndSyncAll(expense: Expense) {
        val budgetQuerySnapshot = firestore.collection("budgets")
            .whereEqualTo("user_id", expense.user_id)
            .whereEqualTo("category", expense.category)
            .get()
            .await()

        if (budgetQuerySnapshot.isEmpty) {
            val newBudget = Budget(
                id = java.util.UUID.randomUUID().toString(),
                user_id = expense.user_id,
                account_id = expense.account_id,
                category = expense.category,
                limit = 0.0,
                spend = expense.price
            )
            // 同时写入 Budget 和 Expense
            firestore.runBatch { batch ->
                val budgetRef = firestore.collection("budgets").document(newBudget.id)
                batch.set(budgetRef, newBudget)

                val expenseRef = firestore.collection("expenses").document(expense.id)
                batch.set(expenseRef, expense)
            }.await()
        } else {
            val budgetDocRef = budgetQuerySnapshot.documents.first().reference

            firestore.runTransaction { transaction ->
                val budgetSnap = transaction.get(budgetDocRef)
                val oldSpend = budgetSnap.getDouble("spend") ?: 0.0
                transaction.update(budgetDocRef, "spend", oldSpend + expense.price)

                val expenseRef = firestore.collection("expenses").document(expense.id)
                transaction.set(expenseRef, expense)

                null
            }.await()
        }
    }

}
