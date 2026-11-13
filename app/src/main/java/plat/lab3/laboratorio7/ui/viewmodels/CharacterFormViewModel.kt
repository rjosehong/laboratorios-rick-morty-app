package plat.lab3.laboratorio7.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import plat.lab3.laboratorio7.data.Character
import plat.lab3.laboratorio7.data.repository.ServiceLocator
import plat.lab3.laboratorio7.navigation.CharacterForm

data class CharacterFormState(
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val gender: String = "",
    val image: String = "",
    val isEditing: Boolean = false
)

class CharacterFormViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repo = ServiceLocator.characterRepository(application)
    private val characterId = savedStateHandle.toRoute<CharacterForm>().id   // 0 = crear
    private var original: Character? = null

    private val _state = MutableStateFlow(CharacterFormState(isEditing = characterId != 0))
    val state: StateFlow<CharacterFormState> = _state.asStateFlow()

    init {
        if (characterId != 0) {
            viewModelScope.launch {
                repo.observeById(characterId).first()?.let { c ->
                    original = c
                    _state.value = CharacterFormState(
                        name = c.name, status = c.status, species = c.species,
                        gender = c.gender, image = c.image, isEditing = true
                    )
                }
            }
        }
    }

    fun onNameChange(v: String) = _state.update { it.copy(name = v) }
    fun onStatusChange(v: String) = _state.update { it.copy(status = v) }
    fun onSpeciesChange(v: String) = _state.update { it.copy(species = v) }
    fun onGenderChange(v: String) = _state.update { it.copy(gender = v) }
    fun onImageChange(v: String) = _state.update { it.copy(image = v) }

    fun save(onDone: () -> Unit) {
        val s = _state.value
        if (s.name.isBlank()) return
        viewModelScope.launch {
            val name = s.name.trim()
            val status = s.status.trim().ifBlank { "unknown" }
            val species = s.species.trim().ifBlank { "unknown" }
            val gender = s.gender.trim().ifBlank { "unknown" }
            val image = s.image.trim()

            if (characterId == 0) {
                repo.add(Character(0, name, status, species, gender, image))
            } else {
                val base = original ?: return@launch
                repo.update(
                    base.copy(name = name, status = status, species = species, gender = gender, image = image)
                )
            }
            onDone()
        }
    }
}