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
import coil.compose.AsyncImage
import plat.lab3.laboratorio7.R
import plat.lab3.laboratorio7.data.CharacterDb
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import plat.lab3.laboratorio7.navigation.BottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCharacters(modifier: Modifier = Modifier,
                  onCharacterClick: (Int) -> Unit = {}) {
    val characters = remember { CharacterDb().getAllCharacters() }

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
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CharactersPreview() {
    Laboratorio7Theme {
        //AppCharacters()
    }
}