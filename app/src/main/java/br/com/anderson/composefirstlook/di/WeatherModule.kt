package br.com.anderson.composefirstlook.di

import br.com.anderson.composefirstlook.data.local.datasource.CityWeatherLocalDataSource
import br.com.anderson.composefirstlook.data.local.datasource.ICityWeatherLocalDataSource
import br.com.anderson.composefirstlook.data.remote.datasource.CityWeatherRemoteDataSource
import br.com.anderson.composefirstlook.data.remote.datasource.ICityWeatherRemoteDataSource
import dagger.Module
import dagger.hilt.InstallIn
import br.com.anderson.composefirstlook.domain.repository.ICityWeatherRepository
import br.com.anderson.composefirstlook.domain.repository.CityWeatherRepository
import dagger.Binds
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    abstract fun provideCityWeatherRemoteDataSource(weatherRemoteDataSource: CityWeatherRemoteDataSource): ICityWeatherRemoteDataSource

    @Binds
    abstract fun provideCityWeatherRepository(weatherRepository: CityWeatherRepository): ICityWeatherRepository

    @Binds
    abstract fun provideCityWeatherLocalDataSource(weatherLocalDataSource: CityWeatherLocalDataSource): ICityWeatherLocalDataSource


}