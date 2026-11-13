package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.laboratorio7.data.repository.LocationFilter
import plat.lab3.laboratorio7.ui.components.EmptyLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.viewmodels.LocationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Locations(
    modifier: Modifier = Modifier,
    onLocationClick: (Int) -> Unit = {},
    viewModel: LocationsViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val filter by viewModel.filter.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    LocationFilter.ALL to "Todas",
                    LocationFilter.FAVORITES to "Favoritas",
                    LocationFilter.NOT_FAVORITES to "No favoritas"
                ).forEach { (option, label) ->
                    FilterChip(
                        selected = filter == option,
                        onClick = { viewModel.setFilter(option) },
                        label = { Text(label) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    state.isLoading -> LoadingLayout(onClick = {})
                    state.data.isEmpty() -> EmptyLayout()
                    else -> LazyColumn {
                        items(state.data, key = { it.id }) { location ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onLocationClick(location.id) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(location.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(location.type, fontSize = 12.sp)
                                }
                                IconButton(onClick = { viewModel.toggleFavorite(location) }) {
                                    Icon(
                                        imageVector = if (location.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                        contentDescription = "Favorita",
                                        tint = if (location.isFavorite) Color.Red else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}