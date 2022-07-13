package br.com.anderson.composefirstlook.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import br.com.anderson.composefirstlook.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.anderson.composefirstlook.presentation.navigation.NavigationKeys
import br.com.anderson.composefirstlook.presentation.navigation.NavigationScreen
import br.com.anderson.composefirstlook.presentation.navigation.NavigationState
import br.com.anderson.composefirstlook.presentation.navigation.NavigationViewModel
import br.com.anderson.composefirstlook.presentation.weather_detail.CityWeatherHistoryDestination
import br.com.anderson.composefirstlook.presentation.weather_detail.WeatherDetailDestination
import br.com.anderson.composefirstlook.presentation.weather_search.WeatherSearchDestination
import br.com.anderson.composefirstlook.presentation.weather_search.WeatherSearchViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                WeatherApp()
            }
        }
    }
}

@Composable
private fun WeatherApp() {
    val navController = rememberNavController()
    val navigationViewModel:NavigationViewModel = hiltViewModel()
    Navigation(navController, navigationViewModel)

    NavHost(navController, startDestination = NavigationScreen.WeatherSearch.route) {
        composable(route = NavigationScreen.WeatherSearch.route) {
            WeatherSearchDestination(navigationViewModel)
        }

        composable(route = NavigationScreen.WeatherHistory.route) {
            CityWeatherHistoryDestination(navController)
        }

        composable(
            route =  "${NavigationScreen.WeatherDetail.route}/{${NavigationKeys.Arg.CITY_NAME}}" ,
            arguments = listOf(navArgument(NavigationKeys.Arg.CITY_NAME) {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            })
        ) { entry ->
            WeatherDetailDestination(navController,
                entry.arguments?.getString(NavigationKeys.Arg.CITY_NAME))
        }
    }
}

@Composable
fun Navigation(navController: NavController, navigationViewModel:NavigationViewModel) {
    val navigation = navigationViewModel.navigation
    LaunchedEffect(navigation) {
        navigation.onEach  { route->
            when (route) {
                is NavigationState.PushNavigationState -> navController.navigate(route.destination)
                is NavigationState.PopNavigationState -> navController.popBackStack()
                else -> { print("do nothing") }
            }
        }.collect()
    }


}