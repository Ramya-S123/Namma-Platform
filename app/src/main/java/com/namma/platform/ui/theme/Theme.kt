package com.namma.platform.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RailwayBlue,
    onPrimary = Color.White,
    primaryContainer = RailwayBlueLight,
    onPrimaryContainer = Color.White,
    secondary = RailwayYellow,
    onSecondary = Color.Black,
    secondaryContainer = RailwayYellowDark,
    onSecondaryContainer = Color.Black,
    background = Color(0xFFF5F7FA),
    onBackground = Color(0xFF1A1A2E),
    surface = Color.White,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFE3F2FD),
    onSurfaceVariant = RailwayBlueDark
)

private val DarkColorScheme = darkColorScheme(
    primary = RailwayBlueLight,
    onPrimary = Color.Black,
    primaryContainer = RailwayBlue,
    onPrimaryContainer = Color.White,
    secondary = RailwayYellow,
    onSecondary = Color.Black,
    secondaryContainer = RailwayYellowDark,
    onSecondaryContainer = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
    surfaceVariant = Color(0xFF263238),
    onSurfaceVariant = RailwayYellow
)

@Composable
fun NammaPlatformTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
