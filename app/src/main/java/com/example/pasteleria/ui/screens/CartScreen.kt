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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pasteleria.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(cartVm: CartViewModel, onBack: () -> Unit) {
    val items = cartVm.items.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Carrito") },
            navigationIcon = {
                Button(onClick = onBack) { Text("Volver") }
            }
        )
    }) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (items.value.isEmpty()) {
                Text("Carrito vacío")
            } else {
                LazyColumn {
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
                Button(onClick = { /* Implement checkout */ }) { Text("Pagar") }
            }
        }
    }
}