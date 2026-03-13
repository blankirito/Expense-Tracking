package com.example.expense_tracking.data.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: ""

                    val user = hashMapOf(
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "profile_picture" to ""
                    )

                    // 先写入 User document
                    db.collection("users").document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            // ✅ 注册成功，开始创建默认账户
                            createDefaultAccounts(uid) { success, error ->
                                if (success) {
                                    onComplete(true, null)
                                } else {
                                    onComplete(false, error ?: "Failed to create default accounts")
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            onComplete(false, e.message)
                        }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    private fun createDefaultAccounts(
        userId: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val accounts = listOf(
            hashMapOf(
                "id" to "account_cash_$userId",
                "user_id" to userId,
                "name" to "Cash",
                "type" to "CASH",
                "currency" to "RM",
                "current_balance" to 0.0
            ),
            hashMapOf(
                "id" to "account_bank_$userId",
                "user_id" to userId,
                "name" to "Bank",
                "type" to "BANK",
                "currency" to "RM",
                "current_balance" to 0.0
            ),
            hashMapOf(
                "id" to "account_ewallet_$userId",
                "user_id" to userId,
                "name" to "Ewallet",
                "type" to "EWALLET",
                "currency" to "RM",
                "current_balance" to 0.0
            )
        )

        val batch = db.batch()
        accounts.forEach { account ->
            val docRef = db.collection("accounts").document(account["id"] as String)
            batch.set(docRef, account)
        }

        // 提交 batch
        batch.commit()
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }
}