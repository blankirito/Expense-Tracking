package com.example.expense_tracking.data.Repository

import com.example.expense_tracking.data.model.Account
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AccountRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getUserAccounts(userId: String): List<Account> {
        return try {
            val snapshot = db.collection("accounts")
                .whereEqualTo("user_id", userId)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(Account::class.java)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun updateUserAccounts(
        userId: String,
        cash: Double,
        bank: Double,
        ewallet: Double
    ): Boolean {
        return try {
            val cashRef = db.collection("accounts")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("type", "CASH")
                .get().await()
                .documents.firstOrNull()?.reference

            val bankRef = db.collection("accounts")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("type", "BANK")
                .get().await()
                .documents.firstOrNull()?.reference

            val ewalletRef = db.collection("accounts")
                .whereEqualTo("user_id", userId)
                .whereEqualTo("type", "EWALLET")
                .get().await()
                .documents.firstOrNull()?.reference

            val batch = db.batch()
            cashRef?.let { batch.update(it, "current_balance", cash) }
            bankRef?.let { batch.update(it, "current_balance", bank) }
            ewalletRef?.let { batch.update(it, "current_balance", ewallet) }

            batch.commit().await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }



}
