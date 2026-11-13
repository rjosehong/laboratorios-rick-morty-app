package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.repository.ServiceLocator
import plat.lab3.laboratorio7.navigation.CharacterDetail

class CharacterDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repo = ServiceLocator.characterRepository(application)
    private val characterId = savedStateHandle.toRoute<CharacterDetail>().id

    val uiState: StateFlow<UiState<Character?>> = repo.observeById(characterId)
        .map { UiState(isLoading = false, data = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState<Character?>(isLoading = true, data = null)
        )

    fun toggleFavorite() {
        val current = uiState.value.data ?: return
        viewModelScope.launch { repo.setFavorite(current.id, !current.isFavorite) }
    }

    fun delete(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.delete(characterId)
            onDone()
        }
    }
}