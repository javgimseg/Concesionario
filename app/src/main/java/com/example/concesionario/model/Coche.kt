package com.example.concesionario.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coches")
data class Coche(
    @PrimaryKey val id: Int,
    val modelo: String,
    val marca: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val valoracion: Double = 0.0,
    val imagen: String = "",
    val esFavorito: Boolean = false
)
