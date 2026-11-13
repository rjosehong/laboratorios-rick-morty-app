package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import plat.lab3.laboratorio7.ui.components.EmptyLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.viewmodels.CharacterDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCharacterDetail(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    viewModel: CharacterDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val character = state.data

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (character != null) {
                        IconButton(onClick = viewModel::toggleFavorite) {
                            Icon(
                                imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorito"
                            )
                        }
                        IconButton(onClick = { onEditClick(character.id) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar")
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                state.isLoading -> LoadingLayout(onClick = {})
                character == null -> EmptyLayout(message = "No se encontró el personaje")
                else -> Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (character.image.isNotBlank()) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(200.dp).clip(CircleShape)
                        )
                    } else {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(200.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(character.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(24.dp))
                    DetailRow("Species:", character.species)
                    DetailRow("Status:", character.status)
                    DetailRow("Gender:", character.gender)
                }
            }
        }
    }

    if (showDeleteDialog && character != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar personaje") },
            text = { Text("¿Seguro que quieres eliminar a ${character.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.delete(onBackClick)
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Text(value, style = MaterialTheme.typography.bodySmall)
    }
}