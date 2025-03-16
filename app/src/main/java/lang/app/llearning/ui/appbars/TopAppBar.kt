package lang.app.llearning.ui.appbars


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lang.app.llearning.R
import lang.app.llearning.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier,tokens:Int,storiesCreated:Int){
    Box(modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, shape = RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp))){
        TopAppBar(
            title = {
                Text("DuoTale")
            },
            modifier.padding(horizontal = 12.dp),
            actions = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.coin_silhouette),
                        modifier = modifier.size(48.dp),
                        contentDescription = "Coin Icon",
                        tint = androidx.compose.ui.graphics.Color.Unspecified
                    )
                    Text(text="$tokens")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Icon(
                        painter = painterResource(R.drawable.history),
                        modifier = modifier.size(40.dp),
                        contentDescription = "Coin Icon",
                        tint = androidx.compose.ui.graphics.Color.Unspecified
                    )
                    Text(text="$storiesCreated")
                }

            }
        )
    }


}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    AppTheme {
        TopBar(tokens = 3000, storiesCreated = 23)
    }
}