package plat.lab3.laboratorio7.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.CharacterDb
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.LocationDb
import plat.lab3.laboratorio7.navigation.CharacterDetail
import plat.lab3.laboratorio7.navigation.LocationDetail

data class CharactersUiState(
    val isLoading: Boolean = true,
    val data: List<Character> = emptyList(),
    val hasError: Boolean = false)

data class CharacterDetailUiState(
    val isLoading: Boolean = true,
    val data: Character? = null,
    val hasError: Boolean = false)

data class LocationsUiState(
    val isLoading: Boolean = true,
    val data: List<Location> = emptyList(),
    val hasError: Boolean = false)

data class LocationDetailUiState(
    val isLoading: Boolean = true,
    val data: Location? = null,
    val hasError: Boolean = false)

class CharactersViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {
    private val characterDb = CharacterDb()
    private val locationDb = LocationDb()

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

    fun loadCharacters(force: Boolean = false) {
        if (!force && charactersJob != null) return
        charactersJob?.cancel()
        _characters.value = CharactersUiState(isLoading = true)
        charactersJob = viewModelScope.launch {
            delay(4000)
            _characters.value = CharactersUiState(isLoading = false, data = characterDb.getAllCharacters())
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
            _characterDetail.value = try {
                CharacterDetailUiState(isLoading = false, data = characterDb.getCharacterById(characterId!!))
            } catch (e: Exception) {
                CharacterDetailUiState(isLoading = false, hasError = true)
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
            _locations.value = LocationsUiState(isLoading = false, data = locationDb.getAllLocations())
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
            _locationDetail.value = try {
                LocationDetailUiState(isLoading = false, data = locationDb.getLocationById(locationId!!))
            } catch (e: Exception) {
                LocationDetailUiState(isLoading = false, hasError = true)
            }
        }
    }

    fun onLocationDetailLoadingClick() {
        if (_locationDetail.value.isLoading) {
            locationDetailJob?.cancel()
            _locationDetail.value = LocationDetailUiState(isLoading = false, hasError = true)
        }
    }
}