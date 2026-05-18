package com.namma.platform.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object TrainDetail : Screen("train_detail/{trainId}") {
        fun createRoute(trainId: Long) = "train_detail/$trainId"
    }
    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Chatbot : Screen("chatbot")
}
