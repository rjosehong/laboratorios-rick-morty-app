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
import plat.lab3.laboratorio7.data.Location
import plat.lab3.laboratorio7.data.repository.LocationFilter
import plat.lab3.laboratorio7.data.repository.ServiceLocator

class LocationsViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = ServiceLocator.locationRepository(application)

    private val _filter = MutableStateFlow(LocationFilter.ALL)
    val filter: StateFlow<LocationFilter> = _filter.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<UiState<List<Location>>> = _filter
        .flatMapLatest { repo.observe(it) }
        .map { UiState(isLoading = false, data = it) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            UiState(isLoading = true, data = emptyList())
        )

    fun setFilter(filter: LocationFilter) {
        _filter.value = filter
    }

    fun toggleFavorite(location: Location) {
        viewModelScope.launch { repo.setFavorite(location.id, !location.isFavorite) }
    }
}