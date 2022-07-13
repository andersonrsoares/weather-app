package br.com.anderson.composefirstlook.presentation.weather_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.anderson.composefirstlook.R;
import br.com.anderson.composefirstlook.domain.model.CityWeather
import br.com.anderson.composefirstlook.presentation.UiState
import br.com.anderson.composefirstlook.ui.theme.BackgroundColor1
import br.com.anderson.composefirstlook.ui.theme.BackgroundColor2
import br.com.anderson.composefirstlook.ui.theme.CardBackgroundColor
import coil.compose.AsyncImage

@Composable
fun WeatherDetailDestination(navController: NavHostController,
                             cityName:String?) {
    WeatherDetailScreen(navController, cityName)
}

@Composable
fun WeatherDetailScreen(navController: NavHostController,
                        cityName: String?) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val viewModel:WeatherDetailViewModel = hiltViewModel()
    // Listen for side effects from the VM

    val weatherState by viewModel.fetchWeatherFlow.collectAsState(initial = UiStateWeatherDetails.Loading)

    LaunchedEffect(true) {
        viewModel.onWeatherSearchClick(cityName.orEmpty())
    }

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        WeatherDetailBody(
            navController,
            weatherState
        )
    }
}


@Composable
fun CityWeatherHistoryDestination(navController: NavHostController) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val viewModel:WeatherDetailViewModel = hiltViewModel()
    // Listen for side effects from the VM

    val weatherState by viewModel.fetchWeatherFlow.collectAsState(initial = UiStateWeatherDetails.Loading)

    LaunchedEffect(true) {
        viewModel.getCityWeatherHistory()
    }

    Scaffold(
        scaffoldState = scaffoldState,
    ) {
        WeatherDetailBody(
            navController,
            weatherState)
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherDetailBody(
    navController: NavHostController,
    weatherState: UiStateWeatherDetails
) {
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxHeight()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundColor1,
                        BackgroundColor2
                    )
                )
            )) {

        Column() {
            Box(Modifier.padding(15.dp)) {
                Button(
                    elevation = null,
                    colors = ButtonDefaults
                        .buttonColors(Color.Transparent),
                    onClick = {
                        navController.popBackStack()
                }) {
                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)
                            .rotate(180f),
                        painter = painterResource(id = R.drawable.arrow),
                        contentDescription = null,
                        tint = Color.White
                    )
                }

            }

            Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)) {
                when(weatherState) {
                    is UiStateWeatherDetails.Loading -> LoadingBar()
                    is UiStateWeatherDetails.Success -> {
                        WeatherList(weatherItems = weatherState.data)
                    }
                    is UiStateWeatherDetails.Failure -> ShowErrorMessage(weatherState.error)
                    is UiStateWeatherDetails.Empty -> ShowMessage(weatherState.message)
                }
            }
        }
    }
}


@Composable
fun WeatherList(
    weatherItems: List<CityWeather> = listOf(CityWeather(
        "good",
        "http://openweathermap.org/img/wn/02d@2x.png",
        temperature = 28.7,
        "curitiba",
        date = 1655568638L),
        CityWeather(
            "good",
            "http://openweathermap.org/img/wn/02d@2x.png",
            temperature = 28.7,
            "curitiba",
            date = 1655568638L))
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(weatherItems) { item ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(CardBackgroundColor)
            ) {
                Box(modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
                ) {
                    Row {
                        AsyncImage(
                            modifier = Modifier
                                .width(40.dp)
                                .height(40.dp),
                            contentScale = ContentScale.FillBounds,
                            model = item.urlIcon,
                            contentDescription = null
                        )
                        Text(
                            modifier = Modifier.align(Alignment.CenterVertically),
                            fontSize = 15.sp,
                            color = Color.White,
                            text = item.description)
                    }


                    Text(color = Color.White,
                        modifier = Modifier.align(Alignment.BottomStart),
                        fontSize = 20.sp,
                        text = item.city)

                    Text(color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 100.sp,
                        text = item.formatTemperature())

                    Text(color = Color.White,
                        modifier = Modifier.align(Alignment.BottomEnd),
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        text = item.formatDate())
                }
            }
        }
    }
}

@Composable
private fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
private fun ShowMessage(error:String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = error)
    }
}

@Composable
private fun ShowErrorMessage(error:String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(color = Color.White,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = error)
    }
}