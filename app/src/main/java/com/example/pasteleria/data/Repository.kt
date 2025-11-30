package com.example.pasteleria.data

import android.content.Context
import com.example.pasteleria.data.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray

class Repository(private val context: Context? = null) {

    private val sampleProducts = listOf(
        Product(1, "Marraqueta de dulce", "Deliciosa marraqueta con dulce", 1200.0, null, "Pan"),
        Product(2, "Torta de vainilla", "Torta mediana para 6 personas", 4500.0, null, "Tortas"),
        Product(3, "Panettone", "Pan dulce tradicional", 3500.0, null, "Dulces")
    )

    suspend fun getProducts(): List<Product> = withContext(Dispatchers.IO) {
        if (context != null) {
            try {
                val stream = context.assets.open("products.json")
                val text = stream.bufferedReader().use { it.readText() }
                val arr = JSONArray(text)
                val list = mutableListOf<Product>()
                for (i in 0 until arr.length()) {
                    val obj = arr.getJSONObject(i)
                    list.add(
                        Product(
                            id = obj.optInt("id", i + 1),
                            name = obj.optString("name"),
                            description = obj.optString("description"),
                            price = obj.optDouble("price", 0.0),
                            imageUrl = obj.optString("image", null),
                            category = obj.optString("category", null)
                        )
                    )
                }
                return@withContext list
            } catch (e: Exception) {
                sampleProducts
            }
        } else {
            sampleProducts
        }
    }

    suspend fun getProductById(id: Int): Product? = getProducts().find { it.id == id }
}
