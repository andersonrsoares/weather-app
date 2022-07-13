package br.com.anderson.composefirstlook.presentation.weather_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.anderson.composefirstlook.R
import br.com.anderson.composefirstlook.domain.DataState
import br.com.anderson.composefirstlook.domain.FailureReason
import br.com.anderson.composefirstlook.domain.model.CityWeather
import br.com.anderson.composefirstlook.domain.repository.ICityWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    private val weatherRepository: ICityWeatherRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {


    private val _fetchWeatherFlow = MutableStateFlow<UiStateWeatherDetails>(UiStateWeatherDetails.Loading)
    val fetchWeatherFlow = _fetchWeatherFlow


    fun onWeatherSearchClick(cityName: String) {
        weatherRepository.fetchWeatherByCity(cityName)
            .onEach {
                when(it) {
                    is DataState.Success -> _fetchWeatherFlow.value  = UiStateWeatherDetails.Success(listOf(it.data))
                    is DataState.Loading -> _fetchWeatherFlow.value  = UiStateWeatherDetails.Loading
                    is DataState.Failure -> _fetchWeatherFlow.value  = getFailureMessage(it)
                }
        }.launchIn(viewModelScope)
    }

    fun getCityWeatherHistory() {
        weatherRepository.fetchWeatherCityHistory()
            .onEach {
                when(it) {
                    is DataState.Loading -> _fetchWeatherFlow.value  = UiStateWeatherDetails.Loading
                    is DataState.Failure -> _fetchWeatherFlow.value  = UiStateWeatherDetails.Failure(context.getString(R.string.error_city_weather))
                    is DataState.Success -> _fetchWeatherFlow.value  =  when {
                        it.data.isNotEmpty() -> UiStateWeatherDetails.Success(it.data)
                        else -> UiStateWeatherDetails.Empty(context.getString(R.string.empty_city_weather_history))
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun getFailureMessage(dateState : DataState.Failure<CityWeather>) : UiStateWeatherDetails.Failure {
       return when(dateState.reason) {
            is FailureReason.ServerError -> UiStateWeatherDetails.Failure(dateState.reason.message)
            else ->  UiStateWeatherDetails.Failure(context.getString(R.string.error_city_weather))
        }
    }
}

sealed class UiStateWeatherDetails  {
    class Success(val data: List<CityWeather>): UiStateWeatherDetails()
    object Loading : UiStateWeatherDetails()
    class Empty(val message: String): UiStateWeatherDetails()
    class Failure(val error: String): UiStateWeatherDetails()
}