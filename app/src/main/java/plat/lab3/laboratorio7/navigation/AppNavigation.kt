package plat.lab3.laboratorio7.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.UserPreferences
import plat.lab3.laboratorio7.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val showBottomBar = destination?.hasRoute<Login>() != true && destination?.hasRoute<CharacterDetail>() != true
            && destination?.hasRoute<LocationDetail>() != true

    val context = LocalContext.current
    val isLocationsOrProfile = destination?.hasRoute<LocationList>() == true ||
            destination?.hasRoute<Profiles>() == true

    BackHandler(enabled = isLocationsOrProfile) {
        (context as? Activity)?.finish()
    }

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
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                var checking by remember { mutableStateOf(true)}

                LaunchedEffect(Unit){
                    val savedName = UserPreferences(context.applicationContext).name.first()
                    if (savedName != null) {
                        navController.navigate(CharacterGraph) {
                            popUpTo<Login> {inclusive = true}
                        }
                    } else {
                        checking = false
                    }
                }

                if (checking) {
                    Box(modifier=Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                } else {
                    AppLogin(
                        onEnterClick = { name ->
                            scope.launch {
                                UserPreferences(context.applicationContext).saveName(name)
                                navController.navigate(CharacterGraph) {
                                    popUpTo<Login> { inclusive = true }
                                }
                            }
                        }
                    )
                }
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
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                val prefs = remember { UserPreferences(context.applicationContext) }
                val name by prefs.name.collectAsState(initial = "")
                Profile(
                    name = name?: "",
                    onLogoutClick = {
                        scope.launch{
                            prefs.clearName()
                            navController.navigate(Login) {
                                popUpTo(navController.graph.id) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}