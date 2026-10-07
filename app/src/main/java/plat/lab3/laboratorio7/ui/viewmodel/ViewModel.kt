package plat.lab3.laboratorio7.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.CharacterDb

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

class CharactersViewModel : ViewModel() {

    private val db = CharacterDb()

    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        loadJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4000)
            _uiState.value = CharactersUiState(
                isLoading = false,
                data = db.getAllCharacters()
            )
        }
    }

    fun loadCharactersDetail() {
        loadJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            _uiState.value = CharactersUiState(
                isLoading = false,
                data = db.getAllCharacters()
            )
        }
    }

    fun loadLocations() {
        loadJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(4000)
            _uiState.value = CharactersUiState(
                isLoading = false,
                data = db.getAllCharacters()
            )
        }
    }

    fun loadLocationsDetail() {
        loadJob?.cancel()
        _uiState.value = CharactersUiState(isLoading = true)
        loadJob = viewModelScope.launch {
            delay(2000)
            _uiState.value = CharactersUiState(
                isLoading = false,
                data = db.getAllCharacters()
            )
        }
    }

    fun onLoadingClick() {
        if (_uiState.value.isLoading) {
            loadJob?.cancel()
            _uiState.value = CharactersUiState(isLoading = false, hasError = true)
        }
    }
}