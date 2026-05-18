package com.namma.platform.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.NammaPrimaryButton
import com.namma.platform.ui.components.NammaTextField
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayBlueDark
import com.namma.platform.ui.theme.RailwayYellow

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) viewModel.clearMessages()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(RailwayBlue.copy(alpha = 0.1f), androidx.compose.ui.graphics.Color.White))
            )
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Namma-Platform",
            style = MaterialTheme.typography.displayLarge,
            color = RailwayBlue,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Login to continue",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))

        NammaTextField(
            value = state.email,
            onValueChange = viewModel::updateEmail,
            label = "Email"
        )
        Spacer(modifier = Modifier.height(16.dp))
        NammaTextField(
            value = state.password,
            onValueChange = viewModel::updatePassword,
            label = "Password",
            isPassword = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { viewModel.resetPassword() }) {
            Text("Forgot Password?", color = RailwayBlue)
        }

        state.errorMessage?.let { msg ->
            Snackbar { Text(msg) }
            Spacer(modifier = Modifier.height(8.dp))
        }
        state.successMessage?.let { msg ->
            Snackbar(containerColor = RailwayYellow) { Text(msg, color = androidx.compose.ui.graphics.Color.Black) }
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        NammaPrimaryButton(
            text = "Login",
            onClick = { viewModel.login(onLoginSuccess) },
            isLoading = state.isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onNavigateToRegister) {
            Text(
                "Don't have an account? Register",
                style = MaterialTheme.typography.bodyLarge,
                color = RailwayBlue,
                textAlign = TextAlign.Center
            )
        }
    }
}
