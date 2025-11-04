package com.example.pasteleria.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pasteleria.data.models.Product
import com.example.pasteleria.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: Int,
    productsVm: ProductsViewModel,
    onAddToCart: (Product) -> Unit,
    onBack: () -> Unit
) {
    // Observe the full list of products and find the one with the matching ID
    val products by productsVm.products.collectAsState()
    val product = products.find { it.id == productId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        product?.let { p ->
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                AsyncImage(
                    model = p.imageUrl,
                    contentDescription = p.name,
                    modifier = Modifier.size(200.dp).fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                Text(p.description)
                Spacer(Modifier.height(12.dp))
                Text("Precio: $${String.format("%.2f", p.price)}")
                Spacer(Modifier.height(16.dp))
                Button(onClick = { onAddToCart(p) }) { Text("Agregar al carrito") }
            }
        } ?: run {
            // Show a loading indicator while the product is being fetched
            CircularProgressIndicator(modifier = Modifier.padding(padding))
        }
    }
}