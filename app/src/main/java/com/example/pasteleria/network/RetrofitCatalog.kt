package com.example.pasteleria.network


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCatalog {

    const val BASE_API    = "https://ms-catalog-service-production.up.railway.app/api/"
    const val BASE_IMAGES = "https://ms-catalog-service-production.up.railway.app"

    private const val BASE_URL = BASE_API



    val api: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }
}
