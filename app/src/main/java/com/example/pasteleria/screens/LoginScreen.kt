package com.example.pasteleria.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.network.RetrofitClient
import com.example.pasteleria.ui.navigation.Screen
import kotlinx.coroutines.launch
import org.json.JSONObject

@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var isErrorLogin by remember { mutableStateOf(false) }
    
    // Estado para controlar si se muestra el diálogo de éxito
    var mostrarDialogoExito by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    fun handleLogin() {
        // Reiniciamos estados
        mensaje = ""
        isErrorLogin = false

        // Limpiamos espacios accidentales al inicio o final
        val correoLimpio = correo.trim()
        val passwordLimpia = password.trim()

        if (correoLimpio.isBlank() || passwordLimpia.isBlank()) {
            mensaje = "Por favor, ingrese correo y contraseña."
            isErrorLogin = true
            return
        }

        scope.launch {
            try {
                val loginData = mapOf("correo" to correoLimpio, "password" to passwordLimpia)
                val response = RetrofitClient.instance.login(loginData)
                
                if (response.isSuccessful && response.body() != null) {
                    val usuario = response.body()!!
                    
                    if (usuario.nombre.isNullOrBlank()) {
                        mensaje = "El correo no está registrado."
                        isErrorLogin = true
                    } else {
                        mensaje = "¡Bienvenido de nuevo, ${usuario.nombre}!"
                        mostrarDialogoExito = true
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = try {
                        if (!errorBody.isNullOrBlank()) {
                            JSONObject(errorBody).optString("message", "")
                        } else ""
                    } catch (e: Exception) {
                        ""
                    }

                    isErrorLogin = true
                    if (response.code() == 401) {
                        mensaje = "Contraseña incorrecta."
                    } else if (response.code() == 404) {
                        mensaje = "El correo no está registrado."
                    } else {
                         mensaje = if (errorMsg.isNotEmpty()) errorMsg else "Error: ${response.code()}"
                    }
                }
            } catch (e: Exception) {
                mensaje = "Error de conexión: ${e.message}"
                isErrorLogin = true
            }
        }
    }

    if (mostrarDialogoExito) {
        AlertDialog(
            onDismissRequest = { 
                mostrarDialogoExito = false 
                navController.navigate(Screen.Inicio.route)
            },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogoExito = false
                    navController.navigate(Screen.Inicio.route)
                }) {
                    Text("Continuar")
                }
            },
            title = { Text("Inicio de Sesión Exitoso") },
            text = { Text(mensaje) }
        )
    }

    Column(modifier = modifier.fillMaxSize()) {
        Navbar(navController)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.pastel1),
                contentDescription = "Logo Pastelería",
                modifier = Modifier
                    .size(150.dp)
                    .padding(vertical = 24.dp)
            )

            Text(
                text = "Inicio de Sesión",
                style = h1Style,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = correo,
                        onValueChange = { 
                            correo = it
                            isErrorLogin = false // Limpiar error al escribir
                        },
                        label = { Text("Correo", style = pStyle) },
                        placeholder = { Text("ejemplo@correo.com", style = pStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        isError = isErrorLogin,
                        trailingIcon = {
                            if (isErrorLogin) Icon(Icons.Default.Warning, "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { 
                            password = it
                            isErrorLogin = false
                        },
                        label = { Text("Contraseña", style = pStyle) },
                        placeholder = { Text("********", style = pStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        isError = isErrorLogin,
                        trailingIcon = {
                            if (isErrorLogin) Icon(Icons.Default.Warning, "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { handleLogin() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Entrar", color = MaterialTheme.colorScheme.onError, style = pStyle)
                    }

                    if (mensaje.isNotEmpty() && !mostrarDialogoExito) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mensaje,
                            style = pStyle,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "¿No tienes cuenta? Regístrate aquí",
                        style = pStyle.copy(color = MaterialTheme.colorScheme.primary, fontSize = 14.sp),
                        modifier = Modifier.clickable { navController.navigate(Screen.Register.route) }
                    )
                }
            }
        }
    }
}