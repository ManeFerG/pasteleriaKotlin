package com.example.pasteleria.data

import com.example.pasteleria.network.RetrofitCatalog

fun Product.urlImagenCompleta(): String {
    val rawPath = imageUrl ?: return ""

    val index = rawPath.indexOf("/images")
    val path = if (index >= 0) rawPath.substring(index) else rawPath

    return when {
        path.startsWith("/images") ->
            RetrofitCatalog.BASE_IMAGES + path

        path.startsWith("images") ->
            "${RetrofitCatalog.BASE_IMAGES}/$path"

        else ->
            "${RetrofitCatalog.BASE_IMAGES}/images/$path"
    }
}
