package fr.onat68.aileronsappmapandroid

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun NavBar(navController: NavHostController) {
    var selectedItem by remember { mutableIntStateOf(1) }
    val items = listOf(NavBarItem.Favorites, NavBarItem.Map, NavBarItem.Species, NavBarItem.News)

    NavigationBar( containerColor = Color(0xff173b65)) {
        //Modifier.height(70.dp),

        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = {
                    if (index == 1 && selectedItem == index) { // block if trying to go to map and map is already displayed
                        return@NavigationBarItem
                    }
                    selectedItem = index
                    navController.navigate(item.route)
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