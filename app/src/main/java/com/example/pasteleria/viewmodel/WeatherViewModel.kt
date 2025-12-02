package com.example.pasteleria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pasteleria.network.WeatherApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class WeatherUiState(
    val isLoading: Boolean = true,
    val temperature: Double? = null,
    val locationName: String? = null,
    val icon: String? = null,
    val error: String? = null
)

class WeatherViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState

    init {
        loadWeather()
    }

    fun loadWeather() {
        _uiState.value = WeatherUiState(isLoading = true)

        viewModelScope.launch {
            try {
                val response = WeatherApiClient.api.getCurrentWeather()
                val icon = iconForWeather(response.currentWeather.weatherCode)

                _uiState.value = WeatherUiState(
                    isLoading = false,
                    temperature = response.currentWeather.temperature,
                    locationName = "Santiago de Chile",
                    icon = icon,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = WeatherUiState(
                    isLoading = false,
                    temperature = null,
                    locationName = null,
                    icon = null,
                    error = "No se pudo obtener el clima"
                )
            }
        }
    }

    //intervalos de la api del clima
    private fun iconForWeather(code: Int): String =
        when (code) {
            0 -> "☀️"
            1, 2 -> "🌤️"
            3 -> "☁️"
            in 51..67 -> "🌧️"
            in 71..77 -> "❄️"
            in 80..82 -> "🌧️"
            else -> "⛅"
        }
}


