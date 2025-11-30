package com.example.pasteleria

import com.example.pasteleria.data.Product
import com.example.pasteleria.viewmodel.CartViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private val testDispatcher = StandardTestDispatcher()

    // Producto de prueba
    private val product1 = Product(1, "Torta 1", "Desc 1", 1000.0, null, "Cat 1")
    private val product2 = Product(2, "Torta 2", "Desc 2", 2000.0, null, "Cat 2")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CartViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregar producto aumenta cantidad si ya existe`() = runTest {
        // Agregar producto 1 vez
        viewModel.add(product1)
        Assert.assertEquals(1, viewModel.items.value.size)
        Assert.assertEquals(1, viewModel.items.value[0].qty)

        // Agregar producto otra vez
        viewModel.add(product1)
        Assert.assertEquals(1, viewModel.items.value.size) // Sigue siendo 1 item en la lista
        Assert.assertEquals(2, viewModel.items.value[0].qty) // Pero cantidad es 2

        // Total debe ser 2000
        Assert.assertEquals(2000.0, viewModel.total(), 0.0)
    }

    @Test
    fun `agregar productos distintos crea items separados`() = runTest {
        viewModel.add(product1)
        viewModel.add(product2)

        Assert.assertEquals(2, viewModel.items.value.size)
        Assert.assertEquals(3000.0, viewModel.total(), 0.0)
    }

    @Test
    fun `disminuir cantidad reduce qty o elimina si es 1`() = runTest {
        viewModel.add(product1)
        viewModel.add(product1) // qty = 2

        viewModel.decrease(product1)
        Assert.assertEquals(1, viewModel.items.value[0].qty)

        viewModel.decrease(product1) // qty era 1, ahora debe eliminarse
        Assert.assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `eliminar producto lo quita del carrito`() = runTest {
        viewModel.add(product1)
        viewModel.add(product2)

        viewModel.remove(product1.id)

        Assert.assertEquals(1, viewModel.items.value.size)
        Assert.assertEquals(product2.id, viewModel.items.value[0].product.id)
    }

    @Test
    fun `vaciar carrito elimina todo`() = runTest {
        viewModel.add(product1)
        viewModel.clear()
        Assert.assertTrue(viewModel.items.value.isEmpty())
    }

    @Test
    fun `disminuir producto inexistente no afecta el carrito`() = runTest {
        viewModel.add(product1)
        val sizeInicial = viewModel.items.value.size
        
        // Intentar disminuir un producto que no está en la lista
        viewModel.decrease(product2)

        // Verificar que nada cambió
        Assert.assertEquals(sizeInicial, viewModel.items.value.size)
        Assert.assertEquals(product1.id, viewModel.items.value[0].product.id)
    }
}