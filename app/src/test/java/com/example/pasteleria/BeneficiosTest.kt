package com.example.pasteleria

import com.example.pasteleria.utils.calcularBeneficios
import org.junit.Assert
import org.junit.Test

class BeneficiosTest {

    @Test
    fun `usuario mayor de 50 obtiene 50 por ciento de descuento`() {
        val (descuento, beneficios) = calcularBeneficios(
            edad = 55,
            correo = "test@correo.com",
            codigo = ""
        )

        Assert.assertEquals(50, descuento)
        Assert.assertTrue(beneficios.any { it.contains("mayor de 50") })
    }

    @Test
    fun `usuario entre 25 y 35 obtiene 20 por ciento de descuento`() {
        val (descuento, beneficios) = calcularBeneficios(
            edad = 30,
            correo = "test@correo.com",
            codigo = ""
        )

        Assert.assertEquals(20, descuento)
        Assert.assertTrue(beneficios.any { it.contains("25-35 años") })
    }

    @Test
    fun `usuario con codigo FELICES50 obtiene 10 por ciento si no tiene otro descuento mayor`() {
        val (descuento, beneficios) = calcularBeneficios(
            edad = 20,
            correo = "test@correo.com",
            codigo = "FELICES50"
        )

        Assert.assertEquals(10, descuento)
        Assert.assertTrue(beneficios.any { it.contains("FELICES50") })
    }

    @Test
    fun `usuario DUOC obtiene torta gratis`() {
        val (_, beneficios) = calcularBeneficios(edad = 20, correo = "alumno@duoc.cl", codigo = "")

        Assert.assertTrue(beneficios.any { it.contains("Torta gratis") })
    }

    @Test
    fun `se aplica el descuento mayor si hay conflicto`() {
        // Usuario de 60 años (50%) con código FELICES50 (10%) -> Debería quedarse con el 50%
        val (descuento, _) = calcularBeneficios(
            edad = 60,
            correo = "test@correo.com",
            codigo = "FELICES50"
        )

        Assert.assertEquals(50, descuento)
    }
}