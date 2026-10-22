package plat.lab3.laboratorio7.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import plat.lab3.laboratorio7.R
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme

@Composable
fun AppLogin(modifier: Modifier = Modifier, isLoading: Boolean = false, onEnterClick: (String) -> Unit = {}) {
    val nameState = rememberTextFieldState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.rickmorty),
                contentDescription = "Rick & Morty logo",
                modifier = Modifier
                    .width(300.dp)
                    .height(180.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                state = nameState,
                enabled = !isLoading,
                placeholder = { Text("Nombre") }
            )

            Spacer(Modifier.height(24.dp))
            Button(
                modifier = Modifier.width(280.dp),
                enabled = !isLoading && nameState.text.isNotBlank(),
                onClick = { onEnterClick(nameState.text.toString().trim()) }
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar Sesión")
                }
            }
        }
        Text(
            text = "Rodrigo José Navas Hong #25589",
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginPreview() {
    Laboratorio7Theme { AppLogin() }
}