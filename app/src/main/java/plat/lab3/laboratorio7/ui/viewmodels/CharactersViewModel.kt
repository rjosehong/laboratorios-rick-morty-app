package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.repository.ServiceLocator

class CharactersViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ServiceLocator.characterRepository(application)

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<List<Character>>> = _showFavoritesOnly
        .flatMapLatest { onlyFavorites ->
            if (onlyFavorites) repo.observeFavorites() else repo.observeAll()
        }
        .map { UiState(isLoading = false, data = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState(isLoading = true, data = emptyList())
        )

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch { repo.setFavorite(character.id, !character.isFavorite) }
    }
}