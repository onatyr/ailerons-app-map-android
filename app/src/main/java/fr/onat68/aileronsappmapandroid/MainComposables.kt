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
    val selectedItem = navBarViewModel.selectedItem.collectAsState(initial = 1)

    NavigationBar( containerColor = Color(0xff173b65)) {
        //Modifier.height(70.dp),

        navBarViewModel.navBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem.value == index,
                onClick = {
                    if (index == 1 && selectedItem.value == index) { // block if trying to go to map and map is already displayed
                        return@NavigationBarItem
                    }
                    navBarViewModel.navigate(index)
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