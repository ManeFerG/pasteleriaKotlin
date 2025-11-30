package com.example.pasteleria

import com.example.pasteleria.utils.ValidatorUtils
import org.junit.Assert
import org.junit.Test

class ValidatorTest {

    @Test
    fun `nombre valido solo contiene letras`() {
        Assert.assertTrue(ValidatorUtils.esNombreValido("Juan Perez"))
        Assert.assertTrue(ValidatorUtils.esNombreValido("Maria Jose"))

        Assert.assertFalse(ValidatorUtils.esNombreValido("Juan123"))
        Assert.assertFalse(ValidatorUtils.esNombreValido("Juan@"))
    }

    @Test
    fun `correo valido tiene formato correcto`() {
        Assert.assertTrue(ValidatorUtils.esCorreoValido("test@correo.com"))
        Assert.assertTrue(ValidatorUtils.esCorreoValido("usuario.nombre@dominio.cl"))

        Assert.assertFalse(ValidatorUtils.esCorreoValido("correosinarooba.com"))
        Assert.assertFalse(ValidatorUtils.esCorreoValido("correo@dominio")) // Falta .com
        Assert.assertFalse(ValidatorUtils.esCorreoValido("@dominio.com"))
    }

    @Test
    fun `password valida tiene al menos 6 caracteres`() {
        Assert.assertTrue(ValidatorUtils.esPasswordValida("123456"))
        Assert.assertTrue(ValidatorUtils.esPasswordValida("passwordSegura"))

        Assert.assertFalse(ValidatorUtils.esPasswordValida("12345"))
        Assert.assertFalse(ValidatorUtils.esPasswordValida(""))
    }

    @Test
    fun `edad valida es numero positivo`() {
        Assert.assertTrue(ValidatorUtils.esEdadValida("25"))
        Assert.assertTrue(ValidatorUtils.esEdadValida("1"))
        Assert.assertTrue(ValidatorUtils.esEdadValida("120"))

        Assert.assertFalse(ValidatorUtils.esEdadValida("0"))
        Assert.assertFalse(ValidatorUtils.esEdadValida("-5"))
        Assert.assertFalse(ValidatorUtils.esEdadValida("veinte"))
        Assert.assertFalse(ValidatorUtils.esEdadValida(""))
    }

    @Test
    fun `telefono es valido si es vacio o numerico`() {
        Assert.assertTrue(ValidatorUtils.esTelefonoValido("")) // Opcional
        Assert.assertTrue(ValidatorUtils.esTelefonoValido("123456789"))

        Assert.assertFalse(ValidatorUtils.esTelefonoValido("123-456"))
        Assert.assertFalse(ValidatorUtils.esTelefonoValido("telefono"))
    }
}