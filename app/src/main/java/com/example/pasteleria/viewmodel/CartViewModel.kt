package com.example.pasteleria.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pasteleria.data.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CartItem(val product: Product, var qty: Int = 1)

class CartViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items

    fun add(product: Product) {
        val current = _items.value.toMutableList()
        val existing = current.find { it.product.id == product.id }
        if (existing != null) existing.qty += 1
        else current.add(CartItem(product, 1))
        _items.value = current
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
