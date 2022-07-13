package br.com.anderson.composefirstlook.presentation.navigation

sealed class NavigationState {
    object EmptyNavigationState: NavigationState()
    object PopNavigationState: NavigationState()
    data class PushNavigationState(val destination: String): NavigationState()
}