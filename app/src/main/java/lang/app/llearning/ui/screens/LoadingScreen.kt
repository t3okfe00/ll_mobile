package lang.app.llearning.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import lang.app.llearning.R

@Composable
fun LoadingScreen(modifier:Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f, // Adjust this value to control the bounce height
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000, // Duration for one cycle
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse // Reverses the animation for the up and down effect
        ), label = ""
    )
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.bookicon),
            contentDescription = "Loading Screen Icon",
            tint = androidx.compose.ui.graphics.Color.Unspecified,
            modifier = modifier.size(60.dp)
                .offset(y = offsetY.dp)
        )
    }
}