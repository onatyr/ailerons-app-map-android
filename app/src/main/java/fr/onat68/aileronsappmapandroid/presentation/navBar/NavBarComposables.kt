package fr.onat68.aileronsappmapandroid.presentation.navBar

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.onat68.aileronsappmapandroid.presentation.NavRoute

@Composable
fun NavBar(navBarViewModel: NavBarViewModel, navigate: (NavRoute) -> Unit) {
    val selectedItem = navBarViewModel.selectedItem.collectAsState(NavBarItem.Map as NavBarItem)

    NavigationBar(containerColor = Color.White, modifier = Modifier.height(60.dp)) {

        NavBarItem.values().forEach { item ->
            NavigationBarItem(
                selected = selectedItem.value == item,
                onClick = {
                    if (item == NavBarItem.Map && selectedItem.value == item) {
                        return@NavigationBarItem
                    }
                    navigate(item.navRoute)
                    navBarViewModel.updateSelectedItem(item)
                },
                icon = {
                    Icon(
                        ImageVector.vectorResource(item.icon),
                        item.title,
                        modifier = Modifier.graphicsLayer(scaleX = 1.2f, scaleY = 1.2f)
                    )
                },
                label = { Text(item.title, fontSize = 14.sp, fontWeight = FontWeight.Normal) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFf19E37),
                    selectedTextColor = Color(0xFFf19E37),
                    indicatorColor = Color.Transparent
                ),
                modifier = Modifier.height(20.dp)
            )
        }
    }
}