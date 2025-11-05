package com.example.pasteleria.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navbar(
    navController: NavController,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.logo_pasteleria),
                    contentDescription = "Logo Pastelería",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "Pastelería Mil Sabores",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        navigationIcon = navigationIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        actions = {
            actions()
            var expanded by remember { mutableStateOf(false) }
            Box {
                Text(
                    text = "Menú",
                    modifier = Modifier
                        .clickable { expanded = true }
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Inicio") },
                        onClick = {
                            navController.navigate(Screen.Inicio.route)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Productos") },
                        onClick = {
                            navController.navigate(Screen.Home.route)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Carro") },
                        onClick = {
                            navController.navigate(Screen.Cart.route)
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Login") },
                        onClick = {
                            navController.navigate(Screen.Login.route)
                            expanded = false
                        }
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}