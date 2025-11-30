package com.example.pasteleria.utils

fun calcularBeneficios(edad: Int, correo: String, codigo: String): Pair<Int, List<String>> {
    var descuento = 0
    val beneficios = mutableListOf<String>()

    // Regla 1: Mayores de 50 años -> 50% descuento
    if (edad >= 50) {
        descuento = 50
        beneficios.add("50% de descuento por ser mayor de 50 años")
    } 
    // Regla 2: Entre 25 y 35 años -> 20% descuento
    else if (edad in 25..35) {
        descuento = 20
        beneficios.add("20% de descuento por edad (25-35 años)")
    }

    if (codigo.trim().uppercase() == "FELICES50") {
        // Aplicamos el máximo descuento posible, si ya tiene 50% o 20%, nos quedamos con el mayor
        descuento = maxOf(descuento, 10)
        beneficios.add("10% de descuento de por vida (FELICES50)")
    }

    if (correo.lowercase().endsWith("@duoc.cl")) {
        beneficios.add("Torta gratis en tu cumpleaños (correo DUOC)")
    }

    return Pair(descuento, beneficios)
}
