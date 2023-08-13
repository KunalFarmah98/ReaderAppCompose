package com.kunalfarmah.apps.readerapp.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.nav.ScreenNames
import kotlinx.coroutines.delay

@Preview
@Composable
fun SplashScreen(navController: NavController = NavController(App.context)) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true){
        scale.animateTo(0.9f, animationSpec = tween(
            800, easing = Easing {
                OvershootInterpolator(8f)
                    .getInterpolation(it)
            }
        ))
        // open next screen after 2 seconds
        delay(2000)
        navController.navigate(ScreenNames.LoginScreen.name)
    }

    Surface(
        modifier = Modifier
            .padding(15.dp)
            .size(330.dp)
            .scale(scale.value),
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(width = 2.dp, color = Color.LightGray)
    ) {
        Column(
            modifier = Modifier.padding(1.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderLogo()
            Spacer(modifier = Modifier.height(15.dp))
            Text(text = "\"Read. Sleep. Repeat\"", modifier = Modifier.padding(horizontal = 10.dp), style = MaterialTheme.typography.headlineMedium, color = Color.LightGray)
        }
    }
}

@Composable
fun ReaderLogo(modifier: Modifier = Modifier){
    Text(text = "Reader Boi", style = MaterialTheme.typography.displayLarge,
        color = Color.Red.copy(0.5f))
}