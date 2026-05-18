package com.namma.platform.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.namma.platform.R
import com.namma.platform.presentation.auth.LoginScreen
import com.namma.platform.presentation.auth.RegisterScreen
import com.namma.platform.presentation.chatbot.ChatbotScreen
import com.namma.platform.presentation.home.HomeScreen
import com.namma.platform.presentation.profile.ProfileScreen
import com.namma.platform.presentation.settings.SettingsScreen
import com.namma.platform.presentation.splash.SplashScreen
import com.namma.platform.presentation.train.TrainDetailScreen

@Composable
fun NammaNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Splash.route,
    isLoggedIn: Boolean
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = listOf(
        Screen.Home.route,
        Screen.Chatbot.route,
        Screen.Profile.route,
        Screen.Settings.route
    )

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary) {
                    val items = listOf(
                        Triple(Screen.Home.route, Icons.Default.Home, R.string.home),
                        Triple(Screen.Chatbot.route, Icons.Default.SmartToy, R.string.chatbot),
                        Triple(Screen.Profile.route, Icons.Default.Person, R.string.profile),
                        Triple(Screen.Settings.route, Icons.Default.Settings, R.string.settings)
                    )
                    items.forEach { (route, icon, labelRes) ->
                        NavigationBarItem(
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(stringResource(labelRes)) },
                            selected = currentRoute == route,
                            onClick = {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(
                    onNavigate = {
                        val dest = if (isLoggedIn) Screen.Home.route else Screen.Login.route
                        navController.navigate(dest) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToRegister = {
                        navController.navigate(Screen.Register.route)
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onNavigateToLogin = { navController.popBackStack() }
                )
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    onTrainClick = { trainId ->
                        navController.navigate(Screen.TrainDetail.createRoute(trainId))
                    }
                )
            }
            composable(
                route = Screen.TrainDetail.route,
                arguments = listOf(navArgument("trainId") { type = NavType.LongType })
            ) { backStackEntry ->
                val trainId = backStackEntry.arguments?.getLong("trainId") ?: 0L
                TrainDetailScreen(
                    trainId = trainId,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
            composable(Screen.Chatbot.route) {
                ChatbotScreen()
            }
        }
    }
}
