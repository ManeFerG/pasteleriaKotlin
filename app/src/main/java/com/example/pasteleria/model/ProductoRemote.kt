package com.example.pasteleria.model

data class ProductoRemote(
    val id: Long,
    val nombreProducto: String,
    val descripcionProducto: String,
    val precioProducto: Double,
    val categoriaId: Long,
    val imagenUrl: String?
)

