package com.example.pasteleria.data

import android.util.Log
import com.example.pasteleria.model.ProductoRemote
import com.example.pasteleria.network.RetrofitCatalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val IMAGE_BASE_URL = "https://ms-catalog-service-production.up.railway.app"

class Repository {

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        val remotos: List<ProductoRemote> = RetrofitCatalog.api.getProductos()

        Log.d("REPO", "Cantidad remota: ${remotos.size}")

        remotos.map { r ->
            val fullUrl = r.imagenUrl?.let { path ->
                // si viene "/images/..." o "images/...", lo normalizamos
                val cleanPath = path.removePrefix("/")
                "$IMAGE_BASE_URL/$cleanPath"
            }

            Log.d("IMG_URL", "Producto ${r.id}: $fullUrl")

            Product(
                id = r.id.toInt(),
                name = r.nombreProducto,
                description = r.descripcionProducto,
                price = r.precioProducto,
                imageUrl = fullUrl,
                category = r.categoriaId.toString()
            )
        }
    }

    suspend fun getProductById(id: Int): Product? =
        getProducts().find { it.id == id }
}
