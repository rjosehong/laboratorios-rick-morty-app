package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.UserPreferences
import plat.lab3.laboratorio7.data.repository.ServiceLocator

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)
    private val characters = ServiceLocator.characterRepository(application)
    private val locations = ServiceLocator.locationRepository(application)

    val name: StateFlow<String> = prefs.name
        .map { it ?: "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            characters.clear()
            locations.clear()
            prefs.clearName()
            onDone()   // navegar solo cuando todo ya se borró
        }
    }
}