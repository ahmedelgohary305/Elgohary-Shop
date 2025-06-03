package com.example.elgoharyshop.auth.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.elgoharyshop.auth.presentation.utils.AuthScreen

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onNavigateToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val state by viewModel.authState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetAuthState()
        }
    }

    AuthScreen(
        title = "Log In",
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onSubmit = { viewModel.login(email, password) },
        toggleToOtherScreenText = "Don't have an account? Sign Up",
        onToggleClick = onNavigateToSignUp,
        state = state,
        isLogin = true,
        authViewModel = viewModel
    )
}
