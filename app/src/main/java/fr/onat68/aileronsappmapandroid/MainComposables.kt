package fr.onat68.aileronsappmapandroid

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun NavBar(navBarViewModel: NavBarViewModel, navHostController: NavHostController) {
    val selectedItem = navBarViewModel.selectedItem.collectAsState(NavBarItem.Map as NavBarItem)

    NavigationBar(containerColor = Color(0xff173b65)) {

        NavBarItem.values().forEach { item ->
            NavigationBarItem(
                selected = selectedItem.value == item,
                onClick = {
                    if (item == NavBarItem.Map && selectedItem.value == item) {
                        return@NavigationBarItem
                    }
                    navHostController.navigate(item.navRoute)
                    navBarViewModel.updateSelectedItem(item)
                },
                icon = {
                    Icon(
                        ImageVector.vectorResource(item.icon),
                        item.title,
                    )
                },
                label = { Text(item.title, fontSize = 12.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFf19E37),
                    selectedTextColor = Color(0xFFf19E37)
                )
            )
        }
    }
}