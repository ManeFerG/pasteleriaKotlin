package com.example.pasteleria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.model.Producto
import java.text.NumberFormat
import java.util.*

@Composable
fun CarroScreen(navController: NavController, modifier: Modifier = Modifier) {
    val carrito = remember {
        mutableStateListOf(
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
    }

    fun handleCantidadChange(index: Int, nuevaCantidad: Int) {
        if (nuevaCantidad < 1) return
        carrito[index] = carrito[index].copy(cantidad = nuevaCantidad)
    }

    fun handleEliminar(index: Int) = carrito.removeAt(index)
    fun limpiarCarrito() = carrito.clear()

    val total = carrito.sumOf { it.precio * it.cantidad }

    Column(modifier.fillMaxSize()) {
        Navbar(navController)

        Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LazyColumn(modifier = Modifier.weight(2f).fillMaxHeight()) {
                if (carrito.isEmpty()) {
                    item {
                        Text(
                            text = "No hay productos en tu carrito.",
                            style = pStyle,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    itemsIndexed(carrito) { index, producto ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = producto.imagen),
                                    contentDescription = producto.nombre,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.width(120.dp).heightIn(80.dp, 150.dp).padding(end = 16.dp)
                                )

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = producto.nombre, style = h1Style)
                                    Text(
                                        text = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                                            .format(producto.precio),
                                        style = pStyle
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        OutlinedTextField(
                                            value = producto.cantidad.toString(),
                                            onValueChange = { value ->
                                                handleCantidadChange(index, value.toIntOrNull() ?: 1)
                                            },
                                            label = { Text("Cantidad", style = pStyle) },
                                            singleLine = true,
                                            modifier = Modifier.width(100.dp)
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))

                                        Button(
                                            onClick = { handleEliminar(index) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.error
                                            ),
                                            modifier = Modifier.height(56.dp)
                                        ) {
                                            Text("Eliminar", color = MaterialTheme.colorScheme.onError, style = pStyle)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Card(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Resumen", style = h1Style)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Total: ${NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(total)}",
                        style = h1Style
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { /* TODO: Proceder al pago */ },
                        enabled = carrito.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFD700))
                    ) {
                        Text("Proceder al pago", color = Color.Black, style = pStyle)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { limpiarCarrito() },
                        enabled = carrito.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpiar carrito", color = MaterialTheme.colorScheme.onError, style = pStyle)
                    }
                }
            }
        }
    }
}