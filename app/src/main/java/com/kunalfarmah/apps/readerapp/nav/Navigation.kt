package com.kunalfarmah.apps.readerapp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kunalfarmah.apps.readerapp.screens.auth.ui.LoginScreen
import com.kunalfarmah.apps.readerapp.screens.home.HomeScreen
import com.kunalfarmah.apps.readerapp.screens.splash.SplashScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunalfarmah.apps.readerapp.screens.details.BookDetailsScreen
import com.kunalfarmah.apps.readerapp.screens.search.SearchScreen
import com.kunalfarmah.apps.readerapp.screens.stats.StatsScreen
import com.kunalfarmah.apps.readerapp.screens.update.UpdateBookScreen

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
        composable(ScreenNames.StatsScreen.name){
            StatsScreen(navController)
        }
        composable(ScreenNames.SearchScreen.name){
            SearchScreen(navController)
        }
        var detailsName = ScreenNames.BookDetailsScreen.name
        composable("$detailsName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){
            it.arguments?.getString("bookId").let{ bookId ->
                BookDetailsScreen(navController = navController, bookId = bookId.toString())
            }
        }

        var updateName = ScreenNames.UpdateScreen.name
        composable("$updateName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){
            it.arguments?.getString("bookId").let{ bookId ->
                UpdateBookScreen(navController = navController, bookId = bookId.toString())
            }
        }
    }
}