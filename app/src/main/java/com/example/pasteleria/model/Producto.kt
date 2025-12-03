package com.example.pasteleria.model


data class Producto(
    val nombre: String,
    val precio: Int,
    val imagen: Int,              // para drawables locales (si quieres)
    val cantidad: Int = 0,
    val imagenUrl: String? = null // URL que viene del microservicio
)
