package fr.onat68.aileronsappmapandroid.presentation.navBar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavBarViewModel : ViewModel() {

    private val _selectedItem = MutableStateFlow(NavBarItem.Map as NavBarItem)
    val selectedItem = _selectedItem.asStateFlow()

    fun updateSelectedItem(navBarItem: NavBarItem) {
        _selectedItem.value = navBarItem
    }
}