package com.namma.platform.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.NammaPrimaryButton
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayYellow

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            tint = RailwayBlue
        )
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = RailwayBlue.copy(alpha = 0.08f))
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Profile", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold,
                    color = RailwayBlue)
                Text("Name", style = MaterialTheme.typography.bodyMedium)
                Text(state.fullName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text("Email", style = MaterialTheme.typography.bodyMedium)
                Text(state.email, style = MaterialTheme.typography.titleLarge)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        NammaPrimaryButton(
            text = "Logout",
            onClick = { viewModel.logout(onLogout) },
            isLoading = state.isLoading
        )
    }
}
