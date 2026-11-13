package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import plat.lab3.laboratorio7.ui.components.EmptyLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCharacters(
    modifier: Modifier = Modifier,
    onCharacterClick: (Int) -> Unit = {},
    onCreateClick: () -> Unit = {},
    viewModel: CharactersViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val onlyFavorites by viewModel.showFavoritesOnly.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                actions = {
                    IconButton(onClick = viewModel::toggleFavoritesFilter) {
                        Icon(
                            imageVector = if (onlyFavorites) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Filtrar favoritos"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClick) {
                Icon(Icons.Default.Add, contentDescription = "Crear personaje")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                state.isLoading -> LoadingLayout(onClick = {})
                state.data.isEmpty() -> EmptyLayout()
                else -> LazyColumn(contentPadding = PaddingValues(bottom = 88.dp)) {
                    items(state.data, key = { it.id }) { character ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCharacterClick(character.id) }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = character.image,
                                contentDescription = character.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.size(48.dp).clip(CircleShape)
                            )
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(character.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${character.species} - ${character.status}", fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(character) }) {
                                Icon(
                                    imageVector = if (character.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = if (character.isFavorite) Color.Red else Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}