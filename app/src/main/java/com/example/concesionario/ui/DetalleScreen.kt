package com.example.concesionario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.concesionario.R
import com.example.concesionario.model.Coche

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(coche: Coche, viewModel: ConcesionarioViewModel, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(coche.modelo) },
                navigationIcon = {
                    IconButton(onClick = { viewModel.cerrarDetalle() }) {
                        Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.cambiarFavorito(coche.id) }) {
                        Icon(
                            painterResource(if (coche.esFavorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border),
                            contentDescription = "Favorito"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = coche.imagen,
                contentDescription = coche.modelo,
                modifier = Modifier.fillMaxWidth().aspectRatio(1.5f),
                contentScale = ContentScale.Crop
            )
            Text(coche.modelo, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(coche.marca, style = MaterialTheme.typography.titleMedium)
            Text("Precio: ${coche.precio} €")
            Text("Valoración: ${coche.valoracion}")
            HorizontalDivider()
            Text(coche.descripcion)
        }
    }
}
