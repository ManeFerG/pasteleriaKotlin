package com.example.pasteleria.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasteleria.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(val product: Product, val qty: Int = 1)

class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    fun add(product: Product) {
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        
        if (index != -1) {
            // Reemplazar el elemento con una copia actualizada
            val existing = current[index]
            current[index] = existing.copy(qty = existing.qty + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _items.value = current
    }

    fun decrease(product: Product) {
        val current = _items.value.toMutableList()
        val index = current.indexOfFirst { it.product.id == product.id }
        
        if (index != -1) {
            val existing = current[index]
            if (existing.qty > 1) {
                // Reemplazar el elemento con una copia actualizada
                current[index] = existing.copy(qty = existing.qty - 1)
                _items.value = current
            } else {
                remove(product.id)
            }
        }
    }

    fun remove(productId: Int) {
        _items.value = _items.value.filterNot { it.product.id == productId }
    }

    fun clear() {
        _items.value = emptyList()
    }

    fun total(): Double {
        return _items.value.sumOf { it.product.price * it.qty }
    }
}
