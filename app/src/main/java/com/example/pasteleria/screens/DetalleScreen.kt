package com.example.pasteleria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.model.ProductoDetalle
import java.text.NumberFormat
import java.util.*

@Composable
fun DetalleScreen(navController: NavController, modifier: Modifier = Modifier) {
    val productos = listOf(
        ProductoDetalle("Torta Cuadrada de Chocolate", 45000, "Deliciosa torta de chocolate con ganache. Personalizable.", R.drawable.cuadrada_chocolate),
        ProductoDetalle("Torta Circular de Vainilla", 40000, "Torta clásica de vainilla, esponjosa y ligera.", R.drawable.circular_vainilla),
        ProductoDetalle("Torta Cuadrada de Frutas", 50000, "Torta decorada con frutas frescas.", R.drawable.cuadrada_frutas),
        ProductoDetalle("Torta Cuadrada de Manjar", 42000, "Relleno cremoso de manjar.", R.drawable.cuadrada_manjar),
        ProductoDetalle("Mousse de Chocolate", 5000, "Postre individual cremoso y suave, hecho con chocolate de alta calidad.", R.drawable.mousse_chocolate),
        ProductoDetalle("Tiramisú Clásico", 5500, "Un postre italiano individual con capas de café, mascarpone y cacao.", R.drawable.tiramisu_clasico),
        ProductoDetalle("Torta Sin Azucar de Naranja", 48000, "Torta endulzada naturalmente, con un toque cítrico y fresco.", R.drawable.sinazucar_naranja),
        ProductoDetalle("Cheesecake Sin Azucar", 47000, "Cremoso cheesecake sobre una base de galletas integrales, sin azúcar añadida.", R.drawable.cheesecake_sinazucar),
        ProductoDetalle("Empanada de Manzana", 3000, "Clásica empanada horneada con un relleno de manzana y canela.", R.drawable.empanada_manzana),
        ProductoDetalle("Tarta de Santiago", 6000, "Tarta de almendras tradicional de Galicia, con un sabor único.", R.drawable.santiago),
        ProductoDetalle("Brownie Sin Gluten", 4000, "Brownie de chocolate denso y húmedo, apto para celíacos.", R.drawable.brownie_singluten),
        ProductoDetalle("Pan sin Gluten", 3500, "Pan artesanal elaborado con harinas sin gluten, de textura suave.", R.drawable.pan_singluten),
        ProductoDetalle("Torta Vegana de Chocolate", 50000, "Bizcocho de chocolate vegano con un rico frosting de cacao, 100% vegetal.", R.drawable.vegana_chocolate),
        ProductoDetalle("Galletas Veganas de Avena", 4500, "Galletas crujientes de avena y pasas, hechas sin ingredientes de origen animal.", R.drawable.galletas_vegana_avena),
        ProductoDetalle("Torta Especial de Cumpleaños", 55000, "Torta personalizable para cumpleaños, con decoración festiva y sabores a elección.", R.drawable.especial_cumpleanos),
        ProductoDetalle("Torta Especial de Boda", 60000, "Elegante torta de varios pisos para bodas, diseñada a medida.", R.drawable.especial_boda)
    )

    Scaffold(
        topBar = { Navbar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painter = painterResource(id = R.drawable.logo_pasteleria),
                    contentDescription = "Logo Pastelería",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(vertical = 16.dp)
                )
                Text(
                    text = "Catálogo Detallado",
                    style = h1Style,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        Image(
                            painter = painterResource(id = producto.imagen),
                            contentDescription = producto.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = NumberFormat.getCurrencyInstance(Locale("es", "CL")).format(producto.precio),
                                style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = producto.descripcion,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}