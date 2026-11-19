package com.example.concesionario.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ConcesionarioApp() {
    val viewModel: ConcesionarioViewModel = viewModel()

    LaunchedEffect(viewModel) {
        viewModel.iniciar()
    }

    val oscuro = when (viewModel.tema) {
        "CLARO" -> false
        "OSCURO" -> true
        else -> isSystemInDarkTheme()
    }

    MaterialTheme(colorScheme = if (oscuro) darkColorScheme() else lightColorScheme()) {
        Surface(Modifier.fillMaxSize()) {
            val cocheSeleccionado = viewModel.coches.firstOrNull { it.id == viewModel.idDetalle }
            val pantallaAncha = LocalConfiguration.current.screenWidthDp >= 600

            if (viewModel.ajustesAbiertos) {
                AjustesScreen(viewModel)
            } else if (pantallaAncha) {
                Row(Modifier.fillMaxSize()) {
                    CatalogoScreen(viewModel, Modifier.weight(1f))
                    VerticalDivider()
                    if (cocheSeleccionado != null) {
                        DetalleScreen(cocheSeleccionado, viewModel, Modifier.weight(1.5f))
                    } else {
                        Box(Modifier.weight(1.5f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                            Text("Elige un coche del catálogo")
                        }
                    }
                }
            } else if (cocheSeleccionado != null) {
                DetalleScreen(cocheSeleccionado, viewModel)
            } else {
                CatalogoScreen(viewModel)
            }
        }
    }
}
