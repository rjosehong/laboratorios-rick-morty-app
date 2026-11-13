package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.laboratorio7.ui.viewmodels.CharacterFormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterFormScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSaved: () -> Unit = {},
    viewModel: CharacterFormViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Editar personaje" else "Crear personaje") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.name, onValueChange = viewModel::onNameChange,
                label = { Text("Nombre") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.status, onValueChange = viewModel::onStatusChange,
                label = { Text("Status") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.species, onValueChange = viewModel::onSpeciesChange,
                label = { Text("Species") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.gender, onValueChange = viewModel::onGenderChange,
                label = { Text("Gender") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.image, onValueChange = viewModel::onImageChange,
                label = { Text("URL de la imagen (opcional)") }, singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { viewModel.save(onSaved) },
                enabled = state.name.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
    }
}