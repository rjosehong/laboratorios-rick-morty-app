package plat.lab3.laboratorio7.ui.components

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.R
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme

@Composable
fun ErrorLayout(onRetryClick: () -> Unit, modifier:Modifier= Modifier){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = plat.lab3.laboratorio7.R.drawable.error_icon),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
                .clip(shape = CircleShape)
        )
        Text("Error al obtener listado de personajes.")
        Text("Intentar de nuevo.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetryClick){
            Text("Reintentar")
        }
    }
}
