package com.example.expense_tracking.data.Repository

import com.google.firebase.firestore.FirebaseFirestore

class RegisterRepository {
    private val db = FirebaseFirestore.getInstance()

    fun registerUser(
        name: String,
        email: String,
        password: String,
        phone: String,
        onComplete: (Boolean, String?) -> Unit
    ) {
        val user = hashMapOf(
            "name" to name,
            "email" to email,
            "password" to password,
            "phone" to phone,
            "profile_picture" to ""
        )

        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    onComplete(false, "Email already exists")
                } else {
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener {onComplete(true, null) }
                            .addOnFailureListener { e ->
                                onComplete(false, e.message)
                            }
                }

            }
            .addOnFailureListener { e ->
                onComplete(false, e.message)
            }
    }
}