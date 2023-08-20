package com.kunalfarmah.apps.readerapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kunalfarmah.apps.readerapp.screens.auth.ui.LoginScreen
import com.kunalfarmah.apps.readerapp.screens.auth.viewmodel.AuthViewModel
import com.kunalfarmah.apps.readerapp.screens.home.HomeScreen
import com.kunalfarmah.apps.readerapp.screens.splash.SplashScreen
import androidx.activity.viewModels
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val user = Firebase.auth.currentUser
    NavHost(navController = navController, startDestination = if(user==null) ScreenNames.SplashScreen.name else ScreenNames.HomeScreen.name){
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