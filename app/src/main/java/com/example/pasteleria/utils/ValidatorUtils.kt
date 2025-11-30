package com.example.pasteleria.utils

object ValidatorUtils {
    fun esNombreValido(nombre: String): Boolean {
        return nombre.matches(Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$"))
    }

    fun esCorreoValido(correo: String): Boolean {
        // Regex mejorado: exige caracteres antes del @, caracteres después, un punto y al menos 2 letras para el dominio (ej: .cl, .com)
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
        return correo.matches(Regex(emailRegex))
    }

    fun esPasswordValida(password: String): Boolean {
        return password.length >= 6
    }

    fun esEdadValida(edad: String): Boolean {
        val edadInt = edad.toIntOrNull()
        return edadInt != null && edadInt > 0 && edadInt <= 120
    }
    
    fun esTelefonoValido(telefono: String): Boolean {
        // El teléfono es opcional, así que si está vacío es válido.
        // Si no está vacío, debe ser numérico.
        return telefono.isBlank() || telefono.matches(Regex("^\\d+$"))
    }
}