package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.local.AppDatabase
import plat.lab3.laboratorio7.data.local.toModel
import plat.lab3.laboratorio7.navigation.CharacterDetail
import plat.lab3.laboratorio7.navigation.LocationDetail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false
)

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false
)

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false
)

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false
)

class CharactersViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val charDao = db.characterDao()
    private val locDao = db.locationDao()

    private val characterId: Int? =
        runCatching { savedStateHandle.toRoute<CharacterDetail>().id }.getOrNull()
    private val locationId: Int? =
        runCatching { savedStateHandle.toRoute<LocationDetail>().id }.getOrNull()

    private val _characters = MutableStateFlow(CharactersUiState())
    val characters: StateFlow<CharactersUiState> = _characters.asStateFlow()
    private var charactersJob: Job? = null

    private val _characterDetail = MutableStateFlow(CharacterDetailUiState())
    val characterDetail: StateFlow<CharacterDetailUiState> = _characterDetail.asStateFlow()
    private var characterDetailJob: Job? = null

    private val _locations = MutableStateFlow(LocationsUiState())
    val locations: StateFlow<LocationsUiState> = _locations.asStateFlow()
    private var locationsJob: Job? = null

    private val _locationDetail = MutableStateFlow(LocationDetailUiState())
    val locationDetail: StateFlow<LocationDetailUiState> = _locationDetail.asStateFlow()
    private var locationDetailJob: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadCharacters(force: Boolean = false) {
        if (!force && charactersJob != null) return
        charactersJob?.cancel()
        _characters.value = CharactersUiState(isLoading = true)
        charactersJob = viewModelScope.launch {
            delay(4000)
            _showFavoritesOnly
                .flatMapLatest { onlyFavorites ->
                    if (onlyFavorites) charDao.getFavorites() else charDao.getAll()
                }
                .collect { entities ->
                    _characters.value = CharactersUiState(
                        isLoading = false,
                        data = entities.map { it.toModel() }
                    )
                }
        }
    }

    fun onCharactersLoadingClick() {
        if (_characters.value.isLoading) {
            charactersJob?.cancel()
            _characters.value = CharactersUiState(isLoading = false, hasError = true)
        }
    }

    fun loadCharacterDetail(force: Boolean = false) {
        if (!force && characterDetailJob != null) return
        characterDetailJob?.cancel()
        _characterDetail.value = CharacterDetailUiState(isLoading = true)
        characterDetailJob = viewModelScope.launch {
            delay(2000)
            val id = characterId
            if (id == null) {
                _characterDetail.value = CharacterDetailUiState(isLoading = false, hasError = true)
                return@launch
            }
            charDao.getById(id).collect { entity ->
                _characterDetail.value =
                    if (entity == null) CharacterDetailUiState(isLoading = false, hasError = true)
                    else CharacterDetailUiState(isLoading = false, data = entity.toModel())
            }
        }
    }

    fun onCharacterDetailLoadingClick() {
        if (_characterDetail.value.isLoading) {
            characterDetailJob?.cancel()
            _characterDetail.value = CharacterDetailUiState(isLoading = false, hasError = true)
        }
    }

    fun loadLocations(force: Boolean = false) {
        if (!force && locationsJob != null) return
        locationsJob?.cancel()
        _locations.value = LocationsUiState(isLoading = true)
        locationsJob = viewModelScope.launch {
            delay(4000)
            locDao.getAll().collect { entities ->
                _locations.value = LocationsUiState(
                    isLoading = false,
                    data = entities.map { it.toModel() }
                )
            }
        }
    }

    fun onLocationsLoadingClick() {
        if (_locations.value.isLoading) {
            locationsJob?.cancel()
            _locations.value = LocationsUiState(isLoading = false, hasError = true)
        }
    }

    fun loadLocationDetail(force: Boolean = false) {
        if (!force && locationDetailJob != null) return
        locationDetailJob?.cancel()
        _locationDetail.value = LocationDetailUiState(isLoading = true)
        locationDetailJob = viewModelScope.launch {
            delay(2000)
            val id = locationId
            if (id == null) {
                _locationDetail.value = LocationDetailUiState(isLoading = false, hasError = true)
                return@launch
            }
            locDao.getById(id).collect { entity ->
                _locationDetail.value =
                    if (entity == null) LocationDetailUiState(isLoading = false, hasError = true)
                    else LocationDetailUiState(isLoading = false, data = entity.toModel())
            }
        }
    }

    fun onLocationDetailLoadingClick() {
        if (_locationDetail.value.isLoading) {
            locationDetailJob?.cancel()
            _locationDetail.value = LocationDetailUiState(isLoading = false, hasError = true)
        }
    }

    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly: StateFlow<Boolean> = _showFavoritesOnly.asStateFlow()

    fun toggleFavoritesFilter() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            charDao.updateFavorite(character.id, !character.isFavorite)
        }
    }
}