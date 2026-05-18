package com.namma.platform

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.namma.platform.navigation.NammaNavGraph
import com.namma.platform.navigation.Screen
import com.namma.platform.presentation.MainViewModel
import com.namma.platform.ui.theme.NammaPlatformTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            NammaPlatformTheme(darkTheme = state.isDarkMode) {
                NammaNavGraph(
                    startDestination = Screen.Splash.route,
                    isLoggedIn = state.isLoggedIn
                )
            }
        }
    }
}
