package com.example.pasteleria.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.data.models.Product
import com.example.pasteleria.viewmodel.ProductsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    productsVm: ProductsViewModel,
    onProductClick: (Int) -> Unit,
    navController: NavController
) {
    val products = productsVm.products.collectAsState()
    val loading = productsVm.loading.collectAsState()

    Scaffold(
        topBar = {
            Navbar(
                navController = navController,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (loading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                ProductList(items = products.value, onClick = { onProductClick(it.id) })
            }
        }
    }
}

@Composable
fun ProductList(items: List<Product>, onClick: (Product) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        items(items) { p ->
            ProductRow(product = p, onClick = onClick)
        }
    }
}

@Composable
fun ProductRow(product: Product, onClick: (Product) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(product) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(product.name)
            Spacer(Modifier.height(4.dp))
            Text("Precio: $${String.format("%.2f", product.price)}")
        }
    }
}