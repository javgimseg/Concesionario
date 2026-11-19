package com.example.concesionario.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.concesionario.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(viewModel: ConcesionarioViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.cerrarAjustes() }) {
                        Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Text("Tema de la aplicación")

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = viewModel.tema == "SISTEMA", onClick = { viewModel.cambiarTema("SISTEMA") })
                Text("Seguir el sistema")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = viewModel.tema == "CLARO", onClick = { viewModel.cambiarTema("CLARO") })
                Text("Claro")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = viewModel.tema == "OSCURO", onClick = { viewModel.cambiarTema("OSCURO") })
                Text("Oscuro")
            }
        }
    }
}
