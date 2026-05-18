package com.namma.platform.presentation.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.SplashLoadingAnimation
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayBlueDark
import com.namma.platform.ui.theme.RailwayYellow
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigate: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onNavigate()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(RailwayBlue, RailwayBlueDark)
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SplashLoadingAnimation()
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Namma-Platform",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "ನಿಮ್ಮ ಸ್ಥಳೀಯ ರೈಲು ಸಹಾಯಕ",
            style = MaterialTheme.typography.titleLarge,
            color = RailwayYellow
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Loading station data…",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}
