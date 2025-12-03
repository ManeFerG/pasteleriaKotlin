package com.example.pasteleria.screens

import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.pasteleria.R
import com.example.pasteleria.components.Navbar
import com.example.pasteleria.components.h1Style
import com.example.pasteleria.components.pStyle
import com.example.pasteleria.model.Usuario
import com.example.pasteleria.network.RetrofitClient
import com.example.pasteleria.utils.calcularBeneficios
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import org.json.JSONObject
import android.Manifest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var mostrarDialogo by remember { mutableStateOf(false) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        imageBitmap = bitmap
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            cameraLauncher.launch()
        } else {
            mensaje = "Se requiere permiso de cámara para tomar la foto."
            mostrarDialogo = true
        }
    }

    fun handleRegister() {
        // Validar campos obligatorios
        if (nombre.isBlank() || correo.isBlank() || direccion.isBlank() || password.isBlank() || edad.isBlank()) {
            mensaje = "Todos los campos obligatorios deben completarse."
            mostrarDialogo = true
            return
        }

        // Validar nombre (solo letras y espacios)
        if (!nombre.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"))) {
            mensaje = "El nombre solo puede contener letras y espacios."
            mostrarDialogo = true
            return
        }

        // Validar formato de correo electrónico
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            mensaje = "El formato del correo electrónico no es válido."
            mostrarDialogo = true
            return
        }

        // Validar longitud de la contraseña
        if (password.length < 6) {
            mensaje = "La contraseña debe tener al menos 6 caracteres."
            mostrarDialogo = true
            return
        }

        // Validar que las contraseñas coincidan
        if (password != confirmPassword) {
            mensaje = "Las contraseñas no coinciden."
            mostrarDialogo = true
            return
        }

        // Validar teléfono (si se ingresa, debe ser numérico)
        if (telefono.isNotBlank() && !telefono.matches(Regex("^\\d+$"))) {
            mensaje = "El teléfono debe contener solo números."
            mostrarDialogo = true
            return
        }

        // Validar edad (debe ser un número válido)
        val edadInt = edad.toIntOrNull()
        if (edadInt == null || edadInt <= 0 || edadInt > 120) {
            mensaje = "Ingrese una edad válida."
            mostrarDialogo = true
            return
        }

        val codigo = ""
        val (descuento, beneficios) = calcularBeneficios(edadInt, correo, codigo)

        val usuario = Usuario(
            nombre = nombre,
            correo = correo,
            password = password,
            telefono = if (telefono.isBlank()) null else telefono,
            direccion = direccion,
            edad = edadInt,
            descuento = descuento,
            beneficios = beneficios,
            fechaRegistro = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        )

        scope.launch {
            try {
                val response = RetrofitClient.instance.registrarUsuario(usuario)
                if (response.isSuccessful) {
                    mensaje = buildString {
                        appendLine("✅ Registro exitoso: ${usuario.nombre}")
                        appendLine("Descuento aplicado: ${usuario.descuento}%")
                        if (usuario.beneficios.isNotEmpty()) {
                            appendLine("Beneficios:")
                            usuario.beneficios.forEach { appendLine("- $it") }
                        } else appendLine("Sin beneficios adicionales.")
                        if (imageBitmap != null) {
                            appendLine("Foto de perfil capturada.")
                        }
                    }
                    
                    nombre = ""
                    correo = ""
                    password = ""
                    confirmPassword = ""
                    telefono = ""
                    direccion = ""
                    edad = ""
                    imageBitmap = null
                } else {
                    // Leemos el cuerpo del error para saber qué pasó en el servidor
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = try {
                        // Intentamos parsear si viene en JSON { "message": "..." }
                        if (!errorBody.isNullOrBlank()) {
                            JSONObject(errorBody).optString("message", errorBody)
                        } else {
                            "Error desconocido"
                        }
                    } catch (e: Exception) {
                        errorBody ?: "Error desconocido"
                    }

                    if (response.code() == 409) {
                        mensaje = "❌ Error: El correo electrónico ya está registrado."
                    } else {
                        mensaje = "Error en el servidor (${response.code()}): $errorMsg"
                    }
                }
            } catch (e: Exception) {
                mensaje = "Error de conexión: ${e.message}"
            }
            mostrarDialogo = true
        }
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
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Usuario",
                style = h1Style,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (imageBitmap != null) {
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(bottom = 8.dp)
                        )
                    } else {
                        Button(
                            onClick = {
                                val hasPermission = ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CAMERA
                                ) == PackageManager.PERMISSION_GRANTED

                                if (hasPermission) {
                                    cameraLauncher.launch()
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                            }
                        ) {
                            Text("Tomar foto de perfil")
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre Completo", style = pStyle) },
                        placeholder = { Text("Tu nombre", style = pStyle) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo Electrónico", style = pStyle) },
                        placeholder = { Text("ejemplo@correo.com", style = pStyle) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", style = pStyle) },
                        placeholder = { Text("********", style = pStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirmar Contraseña", style = pStyle) },
                        placeholder = { Text("********", style = pStyle) },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono (opcional)", style = pStyle) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección", style = pStyle) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = edad,
                        onValueChange = { edad = it },
                        label = { Text("Edad", style = pStyle) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { handleRegister() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Registrarse", color = MaterialTheme.colorScheme.onError, style = pStyle)
                    }
                }
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    confirmButton = {
                        TextButton(onClick = {
                            mostrarDialogo = false
                            if (mensaje.startsWith("✅")) navController.navigate("login")
                        }) {
                            Text("Aceptar", style = pStyle)
                        }
                    },
                    title = { Text("Resultado del Registro", style = h1Style) },
                    text = { Text(mensaje, style = pStyle) }
                )
            }
        }
    }
}