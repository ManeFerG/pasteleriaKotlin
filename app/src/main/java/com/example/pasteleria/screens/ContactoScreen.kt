package com.example.pasteleria.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pasteleria.components.ContactoForm
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoScreen(navController: NavController, modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    
    // Estados para los diálogos
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    fun validateAndSend() {
        // Validar campos vacíos
        if (nombre.isBlank() || correo.isBlank() || mensaje.isBlank()) {
            errorMessage = "Por favor, complete todos los campos para enviar su mensaje."
            showErrorDialog = true
            return
        }
        
        // Validar formato de correo
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            errorMessage = "El formato del correo electrónico no es válido."
            showErrorDialog = true
            return
        }

        // Si pasa las validaciones, mostrar éxito
        showSuccessDialog = true
    }

    Scaffold(
        topBar = {
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
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

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
                onEnviar = { validateAndSend() },
                textStyle = pStyle,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonTextColor = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
        
        // Diálogo de Éxito
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                icon = { 
                    Icon(
                        Icons.Default.CheckCircle, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    ) 
                },
                title = { Text("¡Envío Exitoso!") },
                text = { Text("Gracias por contactarnos. Hemos recibido tu mensaje correctamente.") },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog = false
                        // Limpiar el formulario
                        nombre = ""
                        correo = ""
                        mensaje = ""
                        // Opcional: Volver al inicio
                        navController.popBackStack()
                    }) {
                        Text("Aceptar")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // Diálogo de Error
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                icon = { 
                    Icon(
                        Icons.Default.Warning, 
                        contentDescription = null, 
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(48.dp)
                    ) 
                },
                title = { Text("Atención") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("Entendido")
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}