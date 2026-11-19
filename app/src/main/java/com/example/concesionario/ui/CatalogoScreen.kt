package com.example.concesionario.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concesionario.R
import com.example.concesionario.model.Coche

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(viewModel: ConcesionarioViewModel, modifier: Modifier = Modifier) {
    val coches = if (viewModel.soloFavoritos) viewModel.coches.filter { it.esFavorito } else viewModel.coches
    val mensajeError = viewModel.mensajeError

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Concesionario") },
                actions = {
                    IconButton(onClick = { viewModel.cambiarFiltro() }) {
                        Icon(
                            painterResource(if (viewModel.soloFavoritos) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                            contentDescription = "Solo favoritos"
                        )
                    }
                    IconButton(onClick = { viewModel.actualizarCatalogo() }, enabled = !viewModel.cargando) {
                        Icon(painterResource(R.drawable.ic_refresh), contentDescription = "Actualizar")
                    }
                    IconButton(onClick = { viewModel.abrirAjustes() }) {
                        Icon(painterResource(R.drawable.ic_settings), contentDescription = "Ajustes")
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            viewModel.cargando && viewModel.coches.isEmpty() -> {
                Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            mensajeError != null && viewModel.coches.isEmpty() -> {
                Column(
                    Modifier.fillMaxSize().padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(mensajeError)
                    Button(onClick = { viewModel.actualizarCatalogo() }) { Text("Reintentar") }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(coches, key = { it.id }) { coche ->
                        CocheItem(
                            coche = coche,
                            onClick = { viewModel.abrirDetalle(coche.id) },
                            onToggleFavorito = { viewModel.cambiarFavorito(coche.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CocheItem(coche: Coche, onClick: () -> Unit, onToggleFavorito: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = coche.imagen,
                contentDescription = coche.modelo,
                placeholder = painterResource(R.drawable.ic_car),
                error = painterResource(R.drawable.ic_error_outline),
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(coche.modelo, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(coche.marca)
                Text("${coche.precio} €")
                Text("★ ${coche.valoracion}")
            }
            IconButton(onClick = onToggleFavorito) {
                Icon(
                    painterResource(if (coche.esFavorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                    contentDescription = "Favorito"
                )
            }
        }
    }
}
