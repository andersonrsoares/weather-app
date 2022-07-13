package br.com.anderson.composefirstlook.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor() : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationState>(NavigationState.EmptyNavigationState)
    val navigation = _navigation.asSharedFlow()

    fun push(route: NavigationScreen, args: List<Any>? = null) {
        viewModelScope.launch {
            _navigation.emit(NavigationState.PushNavigationState(extractRoute(route, args)))
        }
    }

    private fun extractRoute(route: NavigationScreen, args: List<Any>? = null) : String{
        return "${route}${args?.let {  "/" + it.joinToString("/") {item-> "$item" }} ?: ""}"
    }

    fun pop() {
        viewModelScope.launch {
            _navigation.emit(NavigationState.PopNavigationState)
        }
    }
}