package com.namma.platform.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.ui.components.LoadingIndicator
import com.namma.platform.ui.components.StationCard
import com.namma.platform.ui.components.TrainCard
import com.namma.platform.ui.theme.RailwayBlue
import com.namma.platform.ui.theme.RailwayYellow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onTrainClick: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val filteredStations = viewModel.getFilteredStations()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(RailwayBlue.copy(alpha = 0.08f), Color.Transparent)
                )
            )
    ) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        "Welcome, ${state.userName}!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (state.isKannada) "ನಮ್ಮ ಪ್ಲಾಟ್‌ಫಾರ್ಮ್" else "Your Local Station Guide",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RailwayYellow
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = RailwayBlue,
                titleContentColor = Color.White
            )
        )

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = viewModel::updateSearch,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(if (state.isKannada) "ನಿಲ್ದಾಣ ಹುಡುಕಿ…" else "Search station…")
                },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RailwayBlue)
            )

            if (state.recentSearches.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Recent: ${state.recentSearches.take(3).joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "Select Station",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                filteredStations.forEach { station ->
                    StationCard(
                        station = station,
                        isSelected = station.id == state.selectedStationId,
                        onClick = { viewModel.selectStation(station) },
                        onFavoriteClick = { viewModel.toggleFavorite(station) },
                        useKannada = state.isKannada
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                if (state.isKannada) "ಮುಂದಿನ 3 ಟ್ರೈನ್‌ಗಳು" else "Next 3 Trains",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = RailwayBlue
            )
        }

        if (state.isLoading) {
            LoadingIndicator(modifier = Modifier.padding(32.dp))
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.trains, key = { it.id }) { train ->
                    TrainCard(
                        train = train,
                        onClick = { onTrainClick(train.id) },
                        onSpeakClick = { viewModel.speakAnnouncement(train) },
                        useKannada = state.isKannada
                    )
                }
            }
        }
    }
}
