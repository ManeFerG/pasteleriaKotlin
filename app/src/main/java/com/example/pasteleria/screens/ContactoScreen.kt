package com.example.pasteleria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.components.ContactoForm
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle

@Composable
fun ContactoScreen(navController: NavController, modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Navbar(navController)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.pastel1),
            contentDescription = "Logo Pastelería",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .heightIn(min = 150.dp)
                .padding(vertical = 24.dp)
        )

        Text(
            text = "Contáctanos",
            style = h1Style
        )

        Spacer(modifier = Modifier.height(16.dp))

        ContactoForm(
            nombre = nombre,
            onNombreChange = { nombre = it },
            correo = correo,
            onCorreoChange = { correo = it },
            mensaje = mensaje,
            onMensajeChange = { mensaje = it },
            onEnviar = {
                println("Mensaje enviado: $mensaje")
                nombre = ""
                correo = ""
                mensaje = ""
            },
            textStyle = pStyle,
            buttonColor = MaterialTheme.colorScheme.primary,
            buttonTextColor = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}