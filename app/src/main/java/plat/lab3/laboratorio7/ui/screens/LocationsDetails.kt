package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.laboratorio7.R
import plat.lab3.laboratorio7.data.LocationDb
import plat.lab3.laboratorio7.ui.components.ErrorLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import plat.lab3.laboratorio7.ui.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsDetails(modifier: Modifier = Modifier,
                     onBackClick: () -> Unit = {},
                     locationId:Int,
                     viewModel: CharactersViewModel = viewModel()){
    val locations = LocationDb().getLocationById(locationId)
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Location details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
            when {
                state.hasError -> ErrorLayout(onRetryClick = viewModel::loadLocationsDetail)
                state.isLoading -> LoadingLayout(onClick = viewModel::onLoadingClick)
                else -> Column(modifier = Modifier
                    .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text(locations.name, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text("ID:", style = MaterialTheme.typography.bodySmall)
                        Text(locations.id.toString(), style = MaterialTheme.typography.bodySmall)
                    }
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text("Type:", style = MaterialTheme.typography.bodySmall)
                        Text(locations.type, style = MaterialTheme.typography.bodySmall)
                    }
                    Row(modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Text("Dimension:", style = MaterialTheme.typography.bodySmall)
                        Text(locations.dimension, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LocationDetailsPreview(){
    Laboratorio7Theme() {
        LocationsDetails(locationId = 2)
    }
}