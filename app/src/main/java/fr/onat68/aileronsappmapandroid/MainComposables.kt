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

@Composable
fun NavBar(navBarViewModel: NavBarViewModel) {
    val selectedItem = navBarViewModel.selectedItem.collectAsState(NavBarItem.Map as NavBarItem)

    NavigationBar(containerColor = Color(0xff173b65)) {
        //Modifier.height(70.dp),

        NavBarItem.values().forEach { item ->
            NavigationBarItem(
                selected = selectedItem.value == item,
                onClick = {
                    if (item == NavBarItem.Map && selectedItem.value == item) {
                        return@NavigationBarItem
                    }
                    navBarViewModel.navigate(item)
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