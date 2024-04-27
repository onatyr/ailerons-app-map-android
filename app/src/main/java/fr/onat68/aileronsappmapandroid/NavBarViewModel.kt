package fr.onat68.aileronsappmapandroid

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class NavBarViewModel(val navController: NavController) : ViewModel() {

    val navBarItems = listOf(
        NavBarItem.Favorites,
        NavBarItem.Map,
        NavBarItem.Species,
        NavBarItem.News
    ) // the order in the list define the order in the composable

    private val _selectedItem: MutableStateFlow<Int> = MutableStateFlow(1)
    val selectedItem: Flow<Int> = _selectedItem

    fun navigate(itemId: Int) {
        switchNavBarItem(itemId)
        navController.navigate(navBarItems[itemId].route)
    }

    fun switchNavBarItem(itemId: Int) {
        _selectedItem.value = itemId
    }
}