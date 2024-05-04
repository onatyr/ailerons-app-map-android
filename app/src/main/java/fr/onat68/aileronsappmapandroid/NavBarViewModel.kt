package fr.onat68.aileronsappmapandroid

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavBarViewModel(private val navController: NavController) : ViewModel() {

    private val _selectedItem = MutableStateFlow(NavBarItem.Map as NavBarItem)
    val selectedItem = _selectedItem.asStateFlow()

    fun navigate(navBarItem: NavBarItem, parameters: String? = null) {
        switchNavBarItem(navBarItem)
        val route =
            if (!parameters.isNullOrEmpty()) "${navBarItem.route}/$parameters" else navBarItem.route
        navController.navigate(route)
    }

    private fun switchNavBarItem(navBarItem: NavBarItem) {
        _selectedItem.value = navBarItem
    }
}