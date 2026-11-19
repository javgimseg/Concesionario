package com.example.concesionario.ui

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.concesionario.data.CochesApiService
import com.example.concesionario.data.ConcesionarioDatabase
import com.example.concesionario.model.Coche
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private val Context.ajustesDataStore by preferencesDataStore(name = "ajustes")
private val CLAVE_TEMA = stringPreferencesKey("tema")

class ConcesionarioViewModel(application: Application) : AndroidViewModel(application) {
    private val api = CochesApiService()
    private val database = Room.databaseBuilder(application, ConcesionarioDatabase::class.java, "concesionario.db").build()
    private val dao = database.cocheDao()
    private var iniciado = false

    var coches by mutableStateOf(emptyList<Coche>())
        private set
    var cargando by mutableStateOf(false)
        private set
    var mensajeError by mutableStateOf<String?>(null)
        private set
    var idDetalle by mutableStateOf<Int?>(null)
        private set
    var soloFavoritos by mutableStateOf(false)
        private set
    var tema by mutableStateOf("SISTEMA")
        private set
    var ajustesAbiertos by mutableStateOf(false)
        private set

    fun iniciar() {
        if (iniciado) return
        iniciado = true
        viewModelScope.launch {
            coches = dao.leerTodos()
            val preferencias = getApplication<Application>().ajustesDataStore.data.first()
            tema = preferencias[CLAVE_TEMA] ?: "SISTEMA"
            if (coches.isEmpty()) actualizarCatalogo()
        }
    }

    fun actualizarCatalogo() {
        viewModelScope.launch {
            cargando = true
            mensajeError = null
            try {
                val idsFavoritos = dao.leerTodos().filter { it.esFavorito }.map { it.id }
                val nuevos = api.getCoches().map { it.copy(esFavorito = it.id in idsFavoritos) }
                dao.guardarTodos(nuevos)
                coches = dao.leerTodos()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                mensajeError = "No se han podido cargar los coches."
            } finally {
                cargando = false
            }
        }
    }

    fun cambiarFavorito(id: Int) {
        val coche = coches.firstOrNull { it.id == id } ?: return
        val nuevoValor = !coche.esFavorito
        coches = coches.map { if (it.id == id) it.copy(esFavorito = nuevoValor) else it }
        viewModelScope.launch { dao.definirFavorito(id, nuevoValor) }
    }

    fun cambiarTema(nuevoTema: String) {
        tema = nuevoTema
        viewModelScope.launch {
            getApplication<Application>().ajustesDataStore.edit { preferencias ->
                preferencias[CLAVE_TEMA] = nuevoTema
            }
        }
    }

    fun abrirDetalle(id: Int) { idDetalle = id }
    fun cerrarDetalle() { idDetalle = null }
    fun cambiarFiltro() { soloFavoritos = !soloFavoritos }
    fun abrirAjustes() { ajustesAbiertos = true }
    fun cerrarAjustes() { ajustesAbiertos = false }
}
