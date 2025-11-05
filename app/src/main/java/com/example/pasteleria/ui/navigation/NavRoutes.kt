package com.example.pasteleria.ui.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object Home : Screen("home")
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: Int) = "product/$productId"
    }
    object Cart : Screen("cart")
    object Login : Screen("login")
    object Register : Screen("register")
}
