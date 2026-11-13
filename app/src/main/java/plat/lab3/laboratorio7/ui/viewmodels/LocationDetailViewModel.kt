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
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.repository.ServiceLocator
import plat.lab3.laboratorio7.navigation.LocationDetail

class LocationDetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repo = ServiceLocator.locationRepository(application)
    private val locationId = savedStateHandle.toRoute<LocationDetail>().id

    val uiState: StateFlow<UiState<Location?>> = repo.observeById(locationId)
        .map { UiState(isLoading = false, data = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState<Location?>(isLoading = true, data = null)
        )

    fun toggleFavorite() {
        val current = uiState.value.data ?: return
        viewModelScope.launch { repo.setFavorite(current.id, !current.isFavorite) }
    }
}