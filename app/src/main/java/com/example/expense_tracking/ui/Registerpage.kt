package com.example.expense_tracking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R
import com.example.expense_tracking.ui.components.EditEmailField
import com.example.expense_tracking.ui.components.EditNameField
import com.example.expense_tracking.ui.components.EditPasswordField
import com.example.expense_tracking.viewmodel.RegisterViewModel

@Composable
fun RegisterPage(
    viewModel: RegisterViewModel = RegisterViewModel(),
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    RegisterCard(
        create_an_account = stringResource(R.string.create_an_account),
        introduction = stringResource(R.string.introduction),
        fullname = viewModel.registerFullname,
        onFullnameChange = { viewModel.registerFullname = it },
        email = viewModel.registerEmail,
        onEmailChange = { viewModel.registerEmail = it },
        password = viewModel.registerPassword,
        onPasswordChange = { viewModel.registerPassword = it },
        confirmPassword = viewModel.confirmPassword,
        onConfirmPasswordChange = { viewModel.confirmPassword = it },
        registerError = viewModel.registerError,
        onRegisterClick = {
            viewModel.register { success ->
                if (success) {
                    onRegisterSuccess()
                }
            }
        },
        onLoginClick = onLoginClick
    )
}

@Composable
fun RegisterCard(
    create_an_account: String,
    introduction: String,
    fullname: String,
    onFullnameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    registerError: String?,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE6F0FA)),
        contentAlignment = Alignment.Center,
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lr),
                    contentDescription = null,
                    modifier = modifier
                        .padding(top = 10.dp, bottom = 10.dp)
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = create_an_account,
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = introduction,
                modifier = modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.fullname),
                modifier = modifier.padding(start = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            EditNameField(
                value = fullname,
                onValueChange = onFullnameChange,
                modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.email),
                modifier = modifier.padding(start = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            EditEmailField(
                value = email,
                onValueChange = onEmailChange,
                modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.password),
                modifier = modifier.padding(start = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            EditPasswordField(
                value = password,
                onValueChange = onPasswordChange,
                modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.confirmPassword),
                modifier = modifier.padding(start = 16.dp)
            )
            EditPasswordField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onRegisterClick() },
                    modifier = modifier.align(Alignment.Center)
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121212),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(stringResource(R.string.create_account))
                }
            }
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            registerError?.let { error ->
                 Text(
                     text = error,
                     color = Color.Red,
                     textAlign = TextAlign.Center,
                     modifier = Modifier
                         .fillMaxWidth()
                         .padding(vertical = 4.dp)
                 )
             }
             Spacer(
                 modifier = Modifier.padding(8.dp)

             )
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.already_have_account),
                    color = Color.Gray
                )
                Button(
                    onClick = { onLoginClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF121212)
                    ),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(stringResource(R.string.sign_in))
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
    ExpenseTrackingApplicationTheme {
        RegisterPage(
            onRegisterSuccess = {},
            onLoginClick = {}
        )
    }
}