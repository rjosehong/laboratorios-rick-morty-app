package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.UserPreferences
import plat.lab3.laboratorio7.data.repository.ServiceLocator

data class LoginUiState(
    val isLoading: Boolean = false,
    val hasError: Boolean = false
)

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)
    private val characters = ServiceLocator.characterRepository(application)
    private val locations = ServiceLocator.locationRepository(application)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    suspend fun hasSession(): Boolean = prefs.name.first() != null

    fun login(name: String, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.value = LoginUiState(isLoading = true)
            try {
                if (!characters.hasData() || !locations.hasData()) {
                    characters.refreshFromApi()
                    locations.refreshFromApi()
                }
                prefs.saveName(name)
                _uiState.value = LoginUiState()
                onSuccess()
            } catch (e: Exception) {
                android.util.Log.e("LoginViewModel", "Fallo al iniciar sesión", e)
                _uiState.value = LoginUiState(hasError = true)
            }
        }
    }
}