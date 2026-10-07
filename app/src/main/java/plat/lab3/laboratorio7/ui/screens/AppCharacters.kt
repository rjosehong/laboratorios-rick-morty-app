package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import plat.lab3.laboratorio7.R
import plat.lab3.laboratorio7.data.CharacterDb
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import plat.lab3.laboratorio7.navigation.BottomBar
import plat.lab3.laboratorio7.ui.components.ErrorLayout
import plat.lab3.laboratorio7.ui.components.LoadingLayout
import plat.lab3.laboratorio7.ui.viewmodels.CharactersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCharacters(modifier: Modifier = Modifier,
                  onCharacterClick: (Int) -> Unit = {},
                  viewModel: CharactersViewModel = viewModel()) {
    val characters = remember { CharacterDb().getAllCharacters() }
    val state by viewModel.characters.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.loadCharacters() }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Characters") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()){
            when {
                state.hasError -> ErrorLayout(onRetryClick = { viewModel.loadCharacters(force = true) })
                state.isLoading -> LoadingLayout(onClick = viewModel::onCharactersLoadingClick)
                else -> LazyColumn(modifier = Modifier) {
                    items(characters) { character ->
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
                            Column {
                                Text(character.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${character.species} - ${character.status}", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersPreview() {
    Laboratorio7Theme {
        //AppCharacters()
    }
}