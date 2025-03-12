package lang.app.llearning.ui.appbars

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import lang.app.llearning.R

@Composable
fun BottomNavigationBar(navController:NavController){

    var selectedItem by remember { mutableIntStateOf(0) }
    val navigationItems = listOf(
        "home" to R.drawable.home,
        "profile" to R.drawable.profile,
        "story" to R.drawable.books
    )
    NavigationBar {
        navigationItems.forEachIndexed { index, (label, iconResId) ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(iconResId),
                        contentDescription = label,
                        modifier = Modifier.size(35.dp)
                    )
                },
                label = { Text(label.uppercase()) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(label)
                }
            )
        }
    }

}