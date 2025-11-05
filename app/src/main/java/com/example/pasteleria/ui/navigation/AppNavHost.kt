package com.example.pasteleria.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleria.screens.InicioScreen
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

    NavHost(navController = nc, startDestination = Screen.Inicio.route) {
        composable(Screen.Inicio.route) {
            InicioScreen(navController = nc)
        }
        composable(Screen.Home.route) {
            HomeScreen(
                productsVm = productsVm,
                onProductClick = { id -> nc.navigate(Screen.ProductDetail.createRoute(id)) },
                navController = nc
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