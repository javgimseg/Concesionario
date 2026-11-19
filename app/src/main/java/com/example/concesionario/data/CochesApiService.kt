package com.example.concesionario.data

import com.example.concesionario.model.Coche
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import org.json.JSONObject

class CochesApiService {
    private val client = HttpClient(Android) {
        expectSuccess = true
    }

    suspend fun getCategoria(categoria: String): List<Coche> {
        val respuesta = client.get("https://dummyjson.com/products/category/$categoria").bodyAsText()

        val productos = JSONObject(respuesta).getJSONArray("products")
        val lista = mutableListOf<Coche>()

        for (i in 0 until productos.length()) {
            val p = productos.getJSONObject(i)
            lista.add(
                Coche(
                    id = p.getInt("id"),
                    modelo = p.getString("title"),
                    marca = p.optString("brand"),
                    descripcion = p.optString("description"),
                    precio = p.optDouble("price"),
                    valoracion = p.optDouble("rating"),
                    imagen = p.optString("thumbnail")
                )
            )
        }
        return lista
    }

    suspend fun getCoches(): List<Coche> = getCategoria("vehicle") + getCategoria("motorcycle")
}
