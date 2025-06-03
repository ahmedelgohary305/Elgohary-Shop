package com.example.elgoharyshop.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.elgoharyshop.GetCustomerQuery.Customer
import com.example.elgoharyshop.auth.domain.AuthRepo
import com.example.elgoharyshop.shop.domain.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
}


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _customer = MutableStateFlow<Customer?>(null)
    val customer: StateFlow<Customer?> = _customer.asStateFlow()

    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = authRepo.signUpCustomer(email, password, firstName, lastName)) {
                is ApiResult.Success -> _authState.value = AuthState.Success("Account created")
                is ApiResult.Error -> _authState.value = AuthState.Error(result.message)
                is ApiResult.Loading -> _authState.value = AuthState.Loading
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            when (val result = authRepo.loginCustomer(email, password)) {
                is ApiResult.Success -> _authState.value = AuthState.Success("Logged in")
                is ApiResult.Error -> _authState.value = AuthState.Error(result.message)
                is ApiResult.Loading -> _authState.value = AuthState.Loading
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            val token = authRepo.getCustomerAccessToken()
            authRepo.logoutCustomer(token!!)
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }

    fun loadCustomer() {
        viewModelScope.launch {
            val user = authRepo.getCustomer()
            _customer.value = user
        }
    }
}
