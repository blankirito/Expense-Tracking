package com.example.expense_tracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.expense_tracking.ui.ExpenseTrackingApplication
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Firebase.firestore

        enableEdgeToEdge()
        setContent {
            ExpenseTrackingApplicationTheme {
                ExpenseTrackingApplication()
            }
        }
    }
}

