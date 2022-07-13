package br.com.anderson.composefirstlook.presentation.navigation

sealed class NavigationScreen(val route: String) {
    object WeatherSearch : NavigationScreen("WEATHER_SEARCH")
    object WeatherDetail : NavigationScreen("WEATHER_DETAIL") {
        fun args(param: String?) = param?.let { listOf<Any>(NavigationKeys.Arg.CITY_NAME to param) } ?: listOf()
    }
    object WeatherHistory : NavigationScreen("WEATHER_HISTORY")

    override fun toString(): String = route
}
internal object NavigationKeys {
    object Arg {
        const val CITY_NAME = "CITY_NAME"
    }
}

