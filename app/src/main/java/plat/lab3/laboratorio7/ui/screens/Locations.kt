package plat.lab3.laboratorio7.ui.screens

import android.app.ProgressDialog.show
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.LocationDb
import plat.lab3.laboratorio7.ui.components.ErrorLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import plat.lab3.laboratorio7.ui.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Locations(modifier:Modifier= Modifier,
              onLocationClick: (Int) -> Unit = {},
              viewModel: CharactersViewModel = viewModel()){
    val locations = remember { LocationDb().getAllLocations() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Locations") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when {
                state.hasError -> ErrorLayout(onRetryClick = viewModel::loadLocations)
                state.isLoading -> LoadingLayout(onClick = viewModel::onLoadingClick)
                else -> LazyColumn(modifier = Modifier) {
                    items(locations) { location ->
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .clickable{onLocationClick(location.id)}){
                            Column(){
                                Text(location.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Text(location.type, fontSize = 12.sp)
                            }
                        }

                    }
                }
            }
        }

    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LocationsPreview(){
    Laboratorio7Theme() {
        Locations()
    }
}