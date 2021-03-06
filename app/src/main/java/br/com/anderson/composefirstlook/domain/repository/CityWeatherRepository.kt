package br.com.anderson.composefirstlook.domain.repository


import br.com.anderson.composefirstlook.data.local.datasource.ICityWeatherLocalDataSource
import br.com.anderson.composefirstlook.data.remote.datasource.RemoteDataSourceError
import br.com.anderson.composefirstlook.data.remote.datasource.RemoteDataSourceResult
import br.com.anderson.composefirstlook.data.remote.datasource.ICityWeatherRemoteDataSource
import br.com.anderson.composefirstlook.domain.DataState
import br.com.anderson.composefirstlook.domain.FailureReason
import br.com.anderson.composefirstlook.domain.converter.ITemperatureConverter
import br.com.anderson.composefirstlook.domain.converter.TemperatureConverter
import br.com.anderson.composefirstlook.domain.model.CityWeather
import br.com.anderson.composefirstlook.domain.model.toCityWeather
import br.com.anderson.composefirstlook.domain.model.toCityWeatherEntity
import br.com.anderson.composefirstlook.util.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CityWeatherRepository @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val weatherRemoteDataSource: ICityWeatherRemoteDataSource,
    private val weatherLocalDataSource: ICityWeatherLocalDataSource,
    private val temperatureConverter: ITemperatureConverter
) : ICityWeatherRepository {

    override fun fetchWeatherByCity(cityName: String): Flow<DataState<CityWeather>> {
        return flow {

            emit(DataState.Loading())

            emit(when(val result = weatherRemoteDataSource.findWeatherByCity(cityName)) {
                is RemoteDataSourceResult.Success -> {
                    weatherLocalDataSource.addCityWeather(result.data.toCityWeatherEntity())
                    DataState.Success(convertTemperature(result.data.toCityWeather()))
                }

                is RemoteDataSourceResult.Error -> DataState.Failure(when(result.error) {
                    is RemoteDataSourceError.NotFound -> FailureReason.ServerError(result.error.message)
                    is RemoteDataSourceError.Unauthorized, RemoteDataSourceError.NetworkError -> FailureReason.NetworkIssue()
                    else -> FailureReason.GenericError()
                })
            })

        }.flowOn(dispatcherProvider.io)
    }

    override fun fetchWeatherCityHistory(): Flow<DataState<List<CityWeather>>> {
        return flow {
            emit(DataState.Loading())
            emit(DataState.Success(weatherLocalDataSource.getLastFiveCitiesSearched().map {
                convertTemperature(it.toCityWeather())
            }))
        }.flowOn(dispatcherProvider.io)
    }

    private fun convertTemperature(cityWeather: CityWeather) : CityWeather {
        return cityWeather.apply {
            temperature = temperatureConverter.convert(temperature)
        }
    }
}