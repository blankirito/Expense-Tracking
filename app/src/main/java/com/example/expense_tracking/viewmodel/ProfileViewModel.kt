package com.example.expense_tracking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expense_tracking.data.UiState.ProfileUiState
import com.example.expense_tracking.data.Repository.AccountRepository
import com.example.expense_tracking.data.Repository.UserRepository
import com.example.expense_tracking.data.model.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val accountRepository: AccountRepository = AccountRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _logoutEvent = MutableStateFlow(false)
    val logoutEvent: StateFlow<Boolean> = _logoutEvent.asStateFlow()

    private var userIdGlobal: String? = null

    fun loadProfile(userId: String) {
        userIdGlobal = userId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val user = userRepository.getUser(userId)
                val accounts = accountRepository.getUserAccounts(userId)

                val cash = accounts.find { it.type == "CASH" }?.current_balance ?: 0.0
                val bank = accounts.find { it.type == "BANK" }?.current_balance ?: 0.0
                val ewallet = accounts.find { it.type == "EWALLET" }?.current_balance ?: 0.0

                _uiState.value = ProfileUiState(
                    userId = userId,
                    name = user?.name ?: "",
                    email = user?.email ?: "",
                    phone = user?.phone ?: "",
                    currency = user?.currency ?: "MYR",
                    cash = cash,
                    bank = bank,
                    ewallet = ewallet,
                    accounts = accounts,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }


    fun updateProfile(name: String, phone: String) {
        val userId = userIdGlobal ?: return
        viewModelScope.launch {
            val success = userRepository.updateProfile(userId, name, phone)
            if (success) {
                _uiState.value = _uiState.value.copy(name = name, phone = phone)
            }
        }
    }


    fun changeEmail(password: String, newEmail: String) {
        val userId = userIdGlobal ?: return
        viewModelScope.launch {
            try {
                // 假设你已经验证 password 对应用户身份
                val success = userRepository.updateEmail(userId, newEmail)
                if (success) {
                    _uiState.value = _uiState.value.copy(email = newEmail)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }




    fun changePassword(newPassword: String) {
        viewModelScope.launch {
            try {
                val success = userRepository.updatePassword(newPassword)
                if (!success) {
                    _uiState.value = _uiState.value.copy(error = "Failed to update password")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }


    fun updateBudget(cash: Double, bank: Double, ewallet: Double) {
        val userId = uiState.value.userId ?: return

        viewModelScope.launch {
            val success = accountRepository.updateUserAccounts(
                userId,
                cash = cash,
                bank = bank,
                ewallet = ewallet
            )
            if (success) {
                _uiState.value = _uiState.value.copy(
                    cash = cash,
                    bank = bank,
                    ewallet = ewallet
                )
            } else {
                _uiState.value = _uiState.value.copy(error = "Failed to save budget")
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
                _uiState.value = ProfileUiState()
                _logoutEvent.value = true
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
