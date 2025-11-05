package com.example.pasteleria.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartVm: CartViewModel, navController: NavController) {
    val items = cartVm.items.collectAsState()

    Scaffold(topBar = {
        Navbar(
            navController = navController,
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack, 
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (items.value.isEmpty()) {
                Text("Carrito vacío")
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(items.value) { it ->
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${it.product.name} x${it.qty}")
                            Text("$${"%.2f".format(it.product.price * it.qty)}")
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                Text("Total: $${"%.2f".format(cartVm.total())}")
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { cartVm.clear() }) { 
                        Text("Vaciar Carrito") 
                    }
                    Button(onClick = { /* Implement checkout */ }) { 
                        Text("Pagar") 
                    }
                }
            }
        }
    }
}