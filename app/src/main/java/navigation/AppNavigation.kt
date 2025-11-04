package navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pasteleria.screens.*

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ) {
        composable("inicio") { InicioScreen(navController) }
        composable("contacto") { ContactoScreen(navController) }
        composable("productos") { ProductoScreen(navController) }
        composable("detalle") { DetalleScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("registro") { RegistroScreen(navController) }
        composable("carro") { CarroScreen(navController) }
    }
}
