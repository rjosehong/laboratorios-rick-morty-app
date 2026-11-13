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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import plat.lab3.laboratorio7.ui.screens.*
import plat.lab3.laboratorio7.ui.viewmodels.LoginViewModel
import plat.lab3.laboratorio7.ui.viewmodels.ProfileViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val destination = backStackEntry?.destination
    val context = LocalContext.current

    val showBottomBar = destination?.hasRoute<Login>() != true &&
            destination?.hasRoute<CharacterDetail>() != true &&
            destination?.hasRoute<CharacterForm>() != true &&
            destination?.hasRoute<LocationDetail>() != true

    val isMainScreen = destination?.hasRoute<CharacterList>() == true ||
            destination?.hasRoute<LocationList>() == true ||
            destination?.hasRoute<Profiles>() == true

    BackHandler(enabled = isMainScreen) { (context as? Activity)?.finish() }

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
                val loginViewModel: LoginViewModel = viewModel()
                val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()
                var checking by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    if (loginViewModel.hasSession()) {
                        navController.navigate(CharacterGraph) {
                            popUpTo<Login> { inclusive = true }
                        }
                    } else {
                        checking = false
                    }
                }

                if (checking) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    AppLogin(
                        isLoading = loginState.isLoading,
                        hasError = loginState.hasError,
                        onEnterClick = { name ->
                            loginViewModel.login(name) {
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
                        onCharacterClick = { id -> navController.navigate(CharacterDetail(id)) },
                        onCreateClick = { navController.navigate(CharacterForm()) }
                    )
                }
                composable<CharacterDetail> {
                    AppCharacterDetail(
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { id -> navController.navigate(CharacterForm(id)) }
                    )
                }
                composable<CharacterForm> {
                    CharacterFormScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaved = { navController.popBackStack() }
                    )
                }
            }

            navigation<LocationGraph>(startDestination = LocationList) {
                composable<LocationList> {
                    Locations(
                        onLocationClick = { id -> navController.navigate(LocationDetail(id)) }
                    )
                }
                composable<LocationDetail> {
                    LocationDetails(onBackClick = { navController.popBackStack() })
                }
            }

            composable<Profiles> {
                val profileViewModel: ProfileViewModel = viewModel()
                val name by profileViewModel.name.collectAsStateWithLifecycle()
                Profile(
                    name = name,
                    onLogoutClick = {
                        profileViewModel.logout {
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