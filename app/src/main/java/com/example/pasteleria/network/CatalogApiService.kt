package com.example.pasteleria.network


import com.example.pasteleria.model.ProductoRemote
import retrofit2.http.GET

interface ProductoApiService {

    @GET("/api/productos")
    suspend fun getProductos(): List<ProductoRemote>
}
