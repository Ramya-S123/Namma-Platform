package com.namma.platform.presentation.train

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.LoadingIndicator
import com.namma.platform.ui.components.StatusBadge
import com.namma.platform.ui.theme.CoachDefault
import com.namma.platform.ui.theme.GeneralCoachGreen
import com.namma.platform.ui.theme.LadiesCoachPink
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainDetailScreen(
    trainId: Long,
    onBack: () -> Unit,
    viewModel: TrainDetailViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Train Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back",
                            tint = RailwayYellow)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RailwayBlue,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        val train = state.train
        if (state.isLoading || train == null) {
            LoadingIndicator(modifier = Modifier.fillMaxSize().padding(padding))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = if (state.isKannada) train.trainNameKannada else train.trainName,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = RailwayBlue
            )
            Text("#${train.trainNumber}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            StatusBadge(train.status)

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(RailwayBlue)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Platform", color = RailwayYellow, style = MaterialTheme.typography.bodyLarge)
                    Text(
                        "${train.platform}",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                if (state.isKannada) "ಬೋಗಿ ವಿನ್ಯಾಸ" else "Coach Layout",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Engine → General → … (scroll →)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(
                    items = train.coachSequence,
                    key = { index, _ -> "coach_$index" }
                ) { index, coach ->
                    val isGeneral = coach.equals("General", ignoreCase = true) ||
                        index == train.generalCoachPosition
                    val isLadies = coach.equals("Ladies", ignoreCase = true) ||
                        index == train.ladiesCoachPosition

                    val bgColor = when {
                        isGeneral -> GeneralCoachGreen
                        isLadies -> LadiesCoachPink
                        coach.equals("Engine", ignoreCase = true) -> RailwayBlue
                        else -> CoachDefault
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (index > 0) {
                            Text(
                                "→",
                                style = MaterialTheme.typography.headlineMedium,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .then(
                                    if (isGeneral || isLadies) Modifier.border(
                                        3.dp, RailwayYellow, RoundedCornerShape(12.dp)
                                    ) else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = coach,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            RowLegend(isKannada = state.isKannada)

            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { viewModel.speakAnnouncement() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RailwayYellow,
                    contentColor = Color.Black
                )
            ) {
                Text("🔊 Speak Announcement (Kannada)", fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun RowLegend(isKannada: Boolean) {
    Column {
        Text(
            if (isKannada) "ಹಸಿರು = ಸಾಮಾನ್ಯ ಬೋಗಿ | ಗುಲಾಬಿ = ಮಹಿಳಾ ಬೋಗಿ"
            else "Green = General Coach | Pink = Ladies Coach",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
