package com.example.pasteleria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.R
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.ui.navigation.Screen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pasteleria.viewmodel.WeatherViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun InicioScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.uiState.collectAsState()
    Column(modifier = modifier.fillMaxSize()) {
        Navbar(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pastel1),
                contentDescription = "Logo Pastelería",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .heightIn(min = 150.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Pastelería Mil Sabores",
                style = h1Style
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pastelería Mil Sabores celebra su 50 aniversario como un referente en la repostería chilena. " +
                        "Famosa por su participación en un récord Guinness en 1995, cuando colaboró en la creación de la " +
                        "torta más grande del mundo, la pastelería busca renovar su sistema de ventas online para ofrecer " +
                        "una experiencia de compra moderna y accesible para sus clientes.",
                style = pStyle
            )

            Spacer(modifier = Modifier.height(24.dp))

                when {
                    weatherState.isLoading -> {
                        CircularProgressIndicator()
                    }

                    weatherState.error != null -> {
                        Text(
                            text = weatherState.error ?: "Error",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    weatherState.temperature != null &&
                            weatherState.locationName != null &&
                            weatherState.icon != null -> {

                        Card(
                            modifier = Modifier
                                .widthIn(max = 420.dp)
                                .fillMaxWidth(0.9f),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFD8F3DC),
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // icono
                                Text(
                                    text = weatherState.icon!!,
                                    fontSize = 32.sp,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // temperatura
                                Text(
                                    text = "${String.format("%.1f", weatherState.temperature)} °C",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                // ciudad
                                Text(
                                    text = weatherState.locationName!!,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Un día ideal para disfrutar algo dulce.",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate(Screen.Home.route) }) {
                    Text("Ver productos")
                }
                Button(onClick = { navController.navigate("contacto") }) {
                    Text("Contacto")
                }
            }
        }
    }}}}