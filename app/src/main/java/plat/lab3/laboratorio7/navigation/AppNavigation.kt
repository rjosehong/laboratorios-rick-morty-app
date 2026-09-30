package plat.lab3.laboratorio7.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import androidx.navigation.toRoute
import plat.lab3.laboratorio7.ui.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {
        composable<Login> {
            AppLogin(
                onEnterClick = {
                    navController.navigate(CharacterList) {
                        popUpTo<Login> { inclusive = true }
                    }
                }
            )
        }
        composable<CharacterList> {
            AppCharacters(onCharacterClick = { id -> navController.navigate(CharacterDetail(id)) })
        }
        composable<CharacterDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<CharacterDetail>()
            AppCharacterDetail(characterId = args.id, onBackClick = { navController.popBackStack() })
        }
    }
}