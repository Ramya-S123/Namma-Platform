package com.namma.platform.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.NammaPrimaryButton
import com.namma.platform.ui.components.NammaTextField
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Register", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateToLogin) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RailwayBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = RailwayYellow
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NammaTextField(value = state.fullName, onValueChange = viewModel::updateFullName, label = "Full Name")
            NammaTextField(value = state.email, onValueChange = viewModel::updateEmail, label = "Email")
            NammaTextField(value = state.password, onValueChange = viewModel::updatePassword, label = "Password", isPassword = true)
            NammaTextField(value = state.confirmPassword, onValueChange = viewModel::updateConfirmPassword, label = "Confirm Password", isPassword = true)

            state.errorMessage?.let { Snackbar { Text(it) } }

            NammaPrimaryButton(
                text = "Register",
                onClick = { viewModel.register(onRegisterSuccess) },
                isLoading = state.isLoading
            )
            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login", color = RailwayBlue, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
