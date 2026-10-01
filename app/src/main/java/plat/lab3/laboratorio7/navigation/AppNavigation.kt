package plat.lab3.laboratorio7.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab3.laboratorio7.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val showBottomBar = destination?.hasRoute<Login>() != true && destination?.hasRoute<CharacterDetail>() != true
            && destination?.hasRoute<LocationDetail>() != true && destination?.hasRoute<Profiles>() != true

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { if (showBottomBar) BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Login,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Login> {
                AppLogin(
                    onEnterClick = {
                        navController.navigate(CharacterGraph) {
                            popUpTo<Login> { inclusive = true }
                        }
                    }
                )
            }

            navigation<CharacterGraph>(startDestination = CharacterList) {
                composable<CharacterList> {
                    AppCharacters(
                        onCharacterClick = { id -> navController.navigate(CharacterDetail(id)) }
                    )
                }
                composable<CharacterDetail> { entry ->
                    val args = entry.toRoute<CharacterDetail>()
                    AppCharacterDetail(
                        characterId = args.id,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            navigation<LocationGraph>(startDestination = LocationList) {
                composable<LocationList> {
                    Locations(
                        onLocationClick = { id -> navController.navigate(LocationDetail(id)) }
                    )
                }
                composable<LocationDetail> { entry ->
                    val args = entry.toRoute<LocationDetail>()
                    LocationsDetails(
                        locationId = args.id,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }

            composable<Profiles> {
                Profile(
                    onLogoutClick = {
                        navController.navigate(Login) {
                            popUpTo(navController.graph.id) { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}