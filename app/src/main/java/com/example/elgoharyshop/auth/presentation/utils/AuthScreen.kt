package com.example.elgoharyshop.auth.presentation.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.elgoharyshop.R
import com.example.elgoharyshop.auth.presentation.AuthState
import com.example.elgoharyshop.auth.presentation.AuthViewModel
import kotlinx.coroutines.delay


@Composable
fun AuthScreen(
    title: String,
    email: String,
    firstName: String = "",
    lastName: String = "",
    onFirstNameChange: (String) -> Unit = {},
    onLastNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    toggleToOtherScreenText: String,
    onToggleClick: () -> Unit,
    state: AuthState,
    isLogin: Boolean,
    authViewModel: AuthViewModel,
) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF8A2387),
                            Color(0xFF0ED2F7),
                            Color(0xFF5EAFAC),
                        )
                    )
                )
                .blur(20.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(10.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        title,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(Font(R.font.poppins_bold))
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = onEmailChange,
                        label = { Text("Email", fontFamily = FontFamily(Font(R.font.poppins_regular))) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    var passwordVisible by remember { mutableStateOf(false) }

                    OutlinedTextField(
                        value = password,
                        onValueChange = onPasswordChange,
                        label = { Text("Password", fontFamily = FontFamily(Font(R.font.poppins_regular))) },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = if (passwordVisible) painterResource(R.drawable.visibility_svgrepo_com) else painterResource(R.drawable.visibility_off_svgrepo_com),
                                    contentDescription = "Toggle Password",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!isLogin){
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = onFirstNameChange,
                            label = { Text("First Name", fontFamily = FontFamily(Font(R.font.poppins_regular))) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = lastName,
                            onValueChange = onLastNameChange,
                            label = { Text("Last Name", fontFamily = FontFamily(Font(R.font.poppins_regular))) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Button(onClick = onSubmit, modifier = Modifier.fillMaxWidth()) {
                        Text(if (isLogin) "Log In" else "Sign Up", fontFamily = FontFamily(Font(R.font.poppins_semi_bold)))
                    }

                    TextButton(onClick = onToggleClick) {
                        Text(toggleToOtherScreenText, fontFamily = FontFamily(Font(R.font.poppins_regular)))
                    }

                    when (state) {
                        is AuthState.Success -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                            LaunchedEffect(state) {
                                delay(2000)
                                authViewModel.resetAuthState()
                            }
                        }
                        is AuthState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                        is AuthState.Error -> {
                            Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()

                            LaunchedEffect(state) {
                                delay(2000)
                                authViewModel.resetAuthState()
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}
