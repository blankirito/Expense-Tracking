package com.example.expense_tracking.data.Constants

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import com.example.expense_tracking.R

object CategoryConstants {
    const val FOOD = "Food"
    const val TRANSPORT = "Transport"
    const val SHOPPING = "Shopping"
    const val BILLS = "Bills"
    const val ENTERTAINMENT = "Entertainment"
    const val HEALTH = "Health"
    const val OTHERS = "Others"

    val CATEGORIES = listOf(FOOD, TRANSPORT, SHOPPING, BILLS, ENTERTAINMENT, HEALTH, OTHERS)

    @SuppressLint("SupportAnnotationUsage")
    @DrawableRes
    val CATEGORY_ICONS = mapOf(
        FOOD to R.drawable.restaurant_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        TRANSPORT to R.drawable.transportation_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        SHOPPING to R.drawable.shopping_bag_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        BILLS to R.drawable.receipt_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        ENTERTAINMENT to R.drawable.sports_esports_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        HEALTH to R.drawable.health_and_safety_24dp_e3e3e3_fill0_wght400_grad0_opsz24,
        OTHERS to R.drawable.view_comfy_alt_24dp_e3e3e3_fill0_wght400_grad0_opsz24
    )
}
