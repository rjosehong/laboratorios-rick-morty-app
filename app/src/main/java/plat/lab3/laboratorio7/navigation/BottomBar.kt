package plat.lab3.laboratorio7.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import plat.lab3.laboratorio7.R
import plat.lab3.laboratorio7.ui.theme.Laboratorio7Theme
import kotlin.reflect.KClass

private data class BottomNavItem(
    val label: String,
    val route: Any,
    val routeClass: KClass<*>,
    val icon: @Composable () -> Unit
)

@Composable
fun BottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val items = listOf(
        BottomNavItem("Characters", CharacterGraph, CharacterGraph::class) {
            Icon(painterResource(R.drawable.characters_icon), contentDescription = null, modifier = Modifier.size(24.dp))
        },
        BottomNavItem("Locations", LocationGraph, LocationGraph::class) {
            Icon(painterResource(R.drawable.locations_icon), contentDescription = null, modifier = Modifier.size(24.dp))
        },
        BottomNavItem("Profile", Profiles, Profiles::class) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(24.dp))
        }
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.hasRoute(item.routeClass) } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo<CharacterGraph> { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = item.icon,
                label = { Text(item.label) }
            )
        }
    }
}