package plat.lab3.laboratorio7.ui.components

import android.R.attr.onClick
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme

@Composable
fun LoadingLayout(onClick: () -> Unit, modifier:Modifier= Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
            .clickable{
                onClick()
            }){
        CircularProgressIndicator()
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Loading"
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun LoadingLayoutPreview(){
    Laboratorio7Theme() {
        LoadingLayout(onClick = {})
    }
}