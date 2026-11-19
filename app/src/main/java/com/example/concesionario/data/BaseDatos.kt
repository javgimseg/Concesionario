package com.example.concesionario.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.concesionario.model.Coche

@Dao
interface CocheDao {
    @Query("SELECT * FROM coches ORDER BY marca")
    suspend fun leerTodos(): List<Coche>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodos(coches: List<Coche>)

    @Query("UPDATE coches SET esFavorito = :favorito WHERE id = :id")
    suspend fun definirFavorito(id: Int, favorito: Boolean)
}

@Database(entities = [Coche::class], version = 1)
abstract class ConcesionarioDatabase : RoomDatabase() {
    abstract fun cocheDao(): CocheDao
}
