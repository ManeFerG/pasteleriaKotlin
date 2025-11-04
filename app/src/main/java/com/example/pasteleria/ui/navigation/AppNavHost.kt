package com.example.pasteleria.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleria.ui.screens.CartScreen
import com.example.pasteleria.ui.screens.HomeScreen
import com.example.pasteleria.ui.screens.LoginScreen
import com.example.pasteleria.ui.screens.ProductDetailScreen
import com.example.pasteleria.viewmodel.CartViewModel
import com.example.pasteleria.viewmodel.ProductsViewModel

@Composable
fun AppNavHost(navController: androidx.navigation.NavHostController? = null) {
    val nc = navController ?: rememberNavController()
    val productsVm: ProductsViewModel = viewModel()
    val cartVm: CartViewModel = viewModel()

    NavHost(navController = nc, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                productsVm = productsVm,
                onProductClick = { id -> nc.navigate(Screen.ProductDetail.createRoute(id)) },
                onCartClick = { nc.navigate(Screen.Cart.route) },
                onLoginClick = { nc.navigate(Screen.Login.route) }
            )
        }

        composable(
            route = "product/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("productId") ?: 0
            ProductDetailScreen(
                productId = id,
                productsVm = productsVm,
                onAddToCart = { product -> cartVm.add(product) },
                onBack = { nc.popBackStack() }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(cartVm = cartVm, onBack = { nc.popBackStack() })
        }

        composable(Screen.Login.route) {
            LoginScreen(onBack = { nc.popBackStack() }, onRegistered = { nc.navigate(Screen.Home.route) })
        }
    }
}