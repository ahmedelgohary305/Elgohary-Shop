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
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    val state by viewModel.authState.collectAsState()

    LaunchedEffect(state) {
        if (state is AuthState.Success) {
            viewModel.resetAuthState()
        }
    }

    AuthScreen(
        title = "Sign Up",
        email = email,
        firstName = firstName,
        lastName = lastName,
        onFirstNameChange = { firstName = it },
        onLastNameChange = { lastName = it },
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        onSubmit = { viewModel.signUp(email, password, firstName, lastName) },
        toggleToOtherScreenText = "Already have an account? Log In",
        onToggleClick = onNavigateToLogin,
        state = state,
        isLogin = false,
        authViewModel = viewModel
    )
}
