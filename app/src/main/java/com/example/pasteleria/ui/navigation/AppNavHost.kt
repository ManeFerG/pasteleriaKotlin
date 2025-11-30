package com.example.pasteleria.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pasteleria.screens.ContactoScreen
import com.example.pasteleria.screens.InicioScreen
import com.example.pasteleria.screens.RegistroScreen
import com.example.pasteleria.screens.CartScreen
import com.example.pasteleria.screens.HomeScreen
import com.example.pasteleria.screens.LoginScreen
import com.example.pasteleria.screens.ProductDetailScreen
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
                onAddToCart = { product -> cartVm.add(product) },
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
                navController = nc
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(cartVm = cartVm, navController = nc)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = nc)
        }

        composable(Screen.Register.route) {
            RegistroScreen(navController = nc)
        }

        composable("contacto") {
            ContactoScreen(navController = nc)
        }
    }
}