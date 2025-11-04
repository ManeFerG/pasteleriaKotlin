package com.example.pasteleria.screens

import com.example.pasteleria.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.model.Producto
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductoScreen(navController: NavController) {
    val productosDisponibles = listOf(
        Producto("Torta Cuadrada de Chocolate", 45000, imagen = R.drawable.cuadrada_chocolate),
        Producto("Torta Circular de Vainilla", 40000, imagen = R.drawable.circular_vainilla),
        Producto("Torta Cuadrada de Frutas", 50000, imagen = R.drawable.cuadrada_frutas),
        Producto("Torta Cuadrada de Manjar", 42000, imagen = R.drawable.cuadrada_manjar),
        Producto("Mousse de Chocolate", 5000, imagen = R.drawable.mousse_chocolate),
        Producto("Tiramisu Clasico", 5500, imagen = R.drawable.tiramisu_clasico),
        Producto("Torta Sin Azucar de Naranja", 48000, imagen = R.drawable.sinazucar_naranja),
        Producto("Cheesecake Sin Azucar", 47000, imagen = R.drawable.cheesecake_sinazucar),
        Producto("Empanada de Manzana", 3000, imagen = R.drawable.empanada_manzana),
        Producto("Tarta de Santiago", 6000, imagen = R.drawable.santiago),
        Producto("Brownie Sin Gluten", 4000, imagen = R.drawable.brownie_singluten),
        Producto("Pan sin Gluten", 3500, imagen = R.drawable.pan_singluten),
        Producto("Torta Vegana de Chocolate", 50000, imagen = R.drawable.vegana_chocolate),
        Producto("Galletas Veganas de Avena", 4500, imagen = R.drawable.galletas_vegana_avena),
        Producto("Torta Especial de Cumpleaños", 55000, imagen = R.drawable.especial_cumpleanos),
        Producto("Torta Especial de Boda", 60000, imagen = R.drawable.especial_boda)
    )

    var carrito by remember { mutableStateOf(listOf<Producto>()) }

    fun agregarAlCarrito(producto: Producto) {
        carrito = carrito.map {
            if (it.nombre == producto.nombre) it.copy(cantidad = it.cantidad + 1) else it
        }.ifEmpty { listOf(producto.copy(cantidad = 1)) }
        if (!carrito.any { it.nombre == producto.nombre }) {
            carrito = carrito + producto.copy(cantidad = 1)
        }
    }

    val total = carrito.sumOf { it.precio * it.cantidad }

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

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                productosDisponibles.forEach { producto ->
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
                            Image(
                                painter = painterResource(id = producto.imagen),
                                contentDescription = producto.nombre,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(text = producto.nombre, style = pStyle)
                            Text(
                                NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                                    .format(producto.precio),
                                style = pStyle
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(onClick = { agregarAlCarrito(producto) }, modifier = Modifier.fillMaxWidth()) {
                                Text("Agregar al carrito 🛒")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Carrito", style = pStyle)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (carrito.isEmpty()) {
                        Text("El carrito está vacío.", style = pStyle)
                    } else {
                        carrito.forEach { p ->
                            Text(
                                "${p.nombre} x ${p.cantidad} = ${
                                    NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(p.precio * p.cantidad)
                                }",
                                style = pStyle
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        Text(
                            "Total: ${NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(total)}",
                            style = pStyle.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}