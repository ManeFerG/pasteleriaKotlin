package com.example.pasteleria.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.data.Product
import com.example.pasteleria.viewmodel.ProductsViewModel
import java.text.NumberFormat
import java.util.*

@Composable
fun ProductoScreen(
    navController: NavController,
    productsVm: ProductsViewModel = viewModel()
) {
    val productos by productsVm.products.collectAsStateWithLifecycle()
    val loading   by productsVm.loading.collectAsStateWithLifecycle()
    val error     by productsVm.error.collectAsStateWithLifecycle()

    var carrito by remember { mutableStateOf(listOf<Product>()) }

    fun agregarAlCarrito(producto: Product) {
        carrito = carrito + producto
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Navbar(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "PRODUCTOS",
                style = h1Style,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 👇 SIEMPRE mostramos esta línea para debug
            Text(
                text = "Productos recibidos: ${productos.size}",
                style = pStyle
            )

            Spacer(modifier = Modifier.height(8.dp))

            when {
                loading -> {
                    Text("Cargando productos...", style = pStyle)
                }

                error != null -> {
                    Text(
                        "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        style = pStyle
                    )
                }

                else -> {
                    if (productos.isEmpty()) {
                        Text("No hay productos para mostrar.", style = pStyle)
                    } else {
                        productos.forEach { producto ->
                            ProductCard(
                                producto = producto,
                                onAgregar = { agregarAlCarrito(producto) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    producto: Product,
    onAgregar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 👇 mostramos la URL de la imagen (debug)
            Text(
                text = producto.imageUrl ?: "sin URL",
                style = pStyle
            )

            AsyncImage(
                model = producto.imageUrl,
                contentDescription = producto.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = producto.name, style = pStyle)

            Text(
                NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                    .format(producto.price),
                style = pStyle
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onAgregar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito 🛒")
            }
        }
    }
}
