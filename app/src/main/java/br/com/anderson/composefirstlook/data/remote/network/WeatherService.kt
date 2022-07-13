package br.com.anderson.composefirstlook.data.remote.network

import br.com.anderson.composefirstlook.data.remote.dto.WeatherContentDto
import retrofit2.Call

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API Service
 */
interface WeatherService {

    @GET("data/2.5/weather")
    suspend fun findWeather(@Query("q") cityName:String) : Response<WeatherContentDto>

}