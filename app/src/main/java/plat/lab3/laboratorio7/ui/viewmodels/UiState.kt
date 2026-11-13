package plat.lab3.laboratorio7.ui.viewmodels

data class UiState<T>(
    val isLoading: Boolean = true,
    val data: T,
    val hasError: Boolean = false
)