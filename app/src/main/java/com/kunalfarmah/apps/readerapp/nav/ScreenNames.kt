package com.kunalfarmah.apps.readerapp.nav

enum class ScreenNames {
    SplashScreen,
    LoginScreen,
    HomeScreen,
    SearchScreen,
    BookDetailsScreen,
    UpdateScreen,
    StatsScreen;

    companion object {
        fun fromRoute(route: String?): ScreenNames = when (route?.substringBefore("/")) {
            SplashScreen.name -> SplashScreen
            LoginScreen.name -> LoginScreen
            HomeScreen.name -> HomeScreen
            SearchScreen.name -> SearchScreen
            BookDetailsScreen.name -> BookDetailsScreen
            UpdateScreen.name -> UpdateScreen
            StatsScreen.name -> StatsScreen
            null -> HomeScreen
            else -> throw IllegalStateException("route doesn't match any screen")
        }
    }
}