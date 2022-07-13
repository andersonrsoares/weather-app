package br.com.anderson.composefirstlook.presentation.weather_search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.anderson.composefirstlook.R;
import br.com.anderson.composefirstlook.presentation.UiState
import br.com.anderson.composefirstlook.presentation.navigation.NavigationScreen
import br.com.anderson.composefirstlook.presentation.navigation.NavigationViewModel
import br.com.anderson.composefirstlook.ui.theme.*
import kotlinx.coroutines.flow.*


@Composable
fun WeatherSearchDestination(navigationViewModel: NavigationViewModel = hiltViewModel()) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        WeatherSearchScreen(navigationViewModel)
    }
}

@Composable
private fun WeatherSearchScreen(
    navigationViewModel: NavigationViewModel = hiltViewModel()
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
        Box(
            Modifier
                .padding(20.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            WeatherSearchScreenBody(navigationViewModel)
        }
    }
}

@Composable
private fun WeatherSearchScreenBody(
    navigationViewModel: NavigationViewModel = hiltViewModel()
) {
    val viewModel:WeatherSearchViewModel = hiltViewModel()
    val validateSearchInput:Flow<UiState<String>> = viewModel.validateSearchInput.receiveAsFlow()

    LaunchedEffect(validateSearchInput) {
        validateSearchInput.onEach  {
            if (it is UiState.Success) {
                navigationViewModel.push(NavigationScreen.WeatherDetail, NavigationScreen.WeatherDetail.args(it.data))
            }
        }.collect()
    }

    WeatherSearchScreenCompose {
        ImageLogo()
        Spacer(modifier = Modifier
            .height(20.dp))
        InputTextContent { search ->
            viewModel.onSearchClick(search)
        }
        Spacer(modifier = Modifier
            .height(20.dp))
        HistoryButton {
            navigationViewModel.push(NavigationScreen.WeatherHistory)
        }
    }
}

@Composable
private fun WeatherSearchScreenCompose(childrenCompose: @Composable () -> Unit) {
    Column(
        Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        childrenCompose()
    }

}

@Composable
private fun ImageLogo() {
    Image(
        painterResource(R.drawable.logo),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}

@Composable
private fun InputTextContent(onClick: (text:String) -> Unit={}) {
    Row {
        val textState = remember { mutableStateOf(TextFieldValue()) }
        TextField(
            modifier = Modifier
                .height(55.dp)
                .background(
                    shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp),
                    color = Color.White
                ),
            placeholder = {
                Text(text = stringResource(R.string.enter_city_name),
                    color = TextColor)
            },
            maxLines = 1,
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                textColor = TextColor,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = TextColor

            ),
            value = textState.value,
            onValueChange = { textState.value = it }
        )

        Spacer(modifier = Modifier
            .width(10.dp))


        Button(modifier = Modifier
            .width(70.dp)
            .height(55.dp),
            shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
            colors = ButtonDefaults
                .buttonColors(Color.White),
            onClick = { onClick(textState.component1().text) }) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = null,
                tint = IconColor
            )
        }
    }
}

@Composable
private fun HistoryButton(onClick: () -> Unit={}) {
    Button(modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth(),
        shape = RoundedCornerShape(6.dp),
        elevation = null,
        colors = ButtonDefaults
            .buttonColors(ButtonBackgroundColor),
        onClick = { onClick() }) {
        Box(modifier = Modifier.padding(start = 15.dp, end = 15.dp, top = 5.dp, bottom = 5.dp)) {
            Text(color = Color.White, text = stringResource(id = R.string.view_history).uppercase())
        }
    }
}
