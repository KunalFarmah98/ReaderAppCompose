package com.kunalfarmah.apps.readerapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kunalfarmah.apps.readerapp.screens.auth.LoginScreen
import com.kunalfarmah.apps.readerapp.screens.home.HomeScreen
import com.kunalfarmah.apps.readerapp.screens.splash.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ScreenNames.SplashScreen.name){
        composable(ScreenNames.SplashScreen.name){
            SplashScreen(navController)
        }
        composable(ScreenNames.LoginScreen.name){
            LoginScreen(navController)
        }
        composable(ScreenNames.HomeScreen.name){
            HomeScreen(navController)
        }
    }
}