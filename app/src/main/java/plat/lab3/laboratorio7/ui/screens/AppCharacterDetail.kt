package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import plat.lab3.laboratorio7.data.CharacterDb
import plat.lab3.laboratorio7.ui.components.ErrorLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import plat.lab3.laboratorio7.ui.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCharacterDetail(characterId: Int,
                       modifier: Modifier = Modifier,
                       onBackClick: () -> Unit = {},
                       viewModel: CharactersViewModel = viewModel()) {
    val character = CharacterDb().getCharacterById(characterId)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
            when {
                state.hasError -> ErrorLayout(onRetryClick = viewModel::loadCharactersDetail)
                state.isLoading -> LoadingLayout(onClick = viewModel::onLoadingClick)
                else -> Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(200.dp).clip(CircleShape)
                    )
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharacterDetailPreview() {
    Laboratorio7Theme { AppCharacterDetail(characterId = 2) }
}