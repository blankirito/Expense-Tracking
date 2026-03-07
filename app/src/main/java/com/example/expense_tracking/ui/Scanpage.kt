package com.example.expense_tracking.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking.data.UiState.ReceiptData
import com.example.expense_tracking.ui.components.EditCategoryField
import com.example.expense_tracking.ui.components.ScanCategoryField
import com.example.expense_tracking.viewmodel.ScanViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@Composable
fun ScanPage(
    modifier: Modifier = Modifier
) {

    val viewModel: ScanViewModel = viewModel()

    var selectedTab by remember { mutableStateOf("Scan") }
    val backgroundColor = Color(0xFFE6F0FA)

    var recognizedText by remember { mutableStateOf("") }
    var receiptData by remember { mutableStateOf(ReceiptData()) }

    val context = LocalContext.current
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    fun runOcr(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val receipt = parseReceipt(visionText.text)
                recognizedText = visionText.text
                receiptData = receipt

                // 填充 ViewModel
                viewModel.onAmountChange(receipt.amount)
                viewModel.onDescriptionChange(receipt.reference)
                // 转换日期格式，例如 "01 Mar 2026" -> "2026-03-01"
                viewModel.onDateChange(parseDateToYMD(receipt.date))
            }
            .addOnFailureListener { e ->
                recognizedText = "OCR Failed: ${e.message}"
            }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = GetContent()
    ) { uri: Uri? ->

        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            selectedBitmap = bitmap
            runOcr(bitmap)
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Add"
                        )
                    },
                    label = { Text("Add") },
                    selected = selectedTab == "Add",
                    onClick = { selectedTab = "Add" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Camera"
                        )
                    },
                    label = { Text("Scan") },
                    selected = selectedTab == "Scan",
                    onClick = { selectedTab = "Scan" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Budget"
                        )
                    },
                    label = { Text("Budget") },
                    selected = selectedTab == "Budget",
                    onClick = { selectedTab = "Budget" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    selected = selectedTab == "Profile",
                    onClick = { selectedTab = "Profile" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Row {
                // icons
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.scan_receipt),
                        fontSize = 25.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = stringResource(R.string.scan_receipt_intro),
                        color = Color(0xFF424242)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Scanpage_CameraPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        bitmap = selectedBitmap
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {

                            @Composable
                            fun RowItem(label: String, value: String) {
                                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$label:",
                                        modifier = Modifier.weight(1f),
                                        fontSize = 14.sp,
                                        color = Color(0xFF1E4EA0),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = value,
                                        modifier = Modifier.weight(2f),
                                        fontSize = 14.sp,
                                        color = Color.Black
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }

                            RowItem("Reference", receiptData.reference)
                            RowItem("Amount", receiptData.amount)
                            RowItem("Date", receiptData.date)
                        }
                    }

                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    ScanCategoryField(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Button(
                        onClick = {
                            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button
                            viewModel.saveExpense(
                                userId = currentUserId,
                                onSuccess = { Toast.makeText(context, "Expense saved", Toast.LENGTH_SHORT).show() },
                                onError = { e -> Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show() }
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1565C0),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.save_expense)
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.upload_from_gallery)
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Scanpage_tips(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

fun parseDateToYMD(dateStr: String): String {
    return try {
        val inputFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.ENGLISH)
        val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.ENGLISH)
        val date = inputFormat.parse(dateStr)
        outputFormat.format(date)
    } catch (e: Exception) {
        ""
    }
}

fun parseReceipt(ocrText: String): ReceiptData {
    val lines = ocrText.lines()
    var reference = ""
    var amount = ""
    var date = ""
    val dateRegex = Regex("""\d{1,2} [A-Za-z]{3} \d{4}""")

    for (i in lines.indices) {
        val line = lines[i].trim()

        if (line.contains("Reference", ignoreCase = true) || line.contains("Recipient", ignoreCase = true)) {
            if (i + 1 < lines.size) reference = lines[i + 1].trim()
        }

        if (line.contains("Amount", ignoreCase = true)) {
            if (i + 1 < lines.size) amount = lines[i + 1].trim()
        }

        val match = dateRegex.find(line)
        if (match != null) date = match.value
    }

    return ReceiptData(reference, amount, date)
}

@Composable
fun Scanpage_CameraPreview(
    modifier: Modifier = Modifier,
    bitmap: Bitmap?
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Selected picture will display at here")
            }
        }
    }
}

@Composable
fun Scanpage_tips(
    modifier: Modifier = Modifier
) {
    Card (
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0E2F8),
            contentColor = Color(0xFF1E4EA0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.scan_tip_1),
                fontSize = 12.sp
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.scan_tip_2),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_3),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_4),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_5),
                fontSize = 12.sp
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ScanPreview() {
    ExpenseTrackingApplicationTheme {
        ScanPage()
    }
}