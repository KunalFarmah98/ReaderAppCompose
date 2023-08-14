package com.kunalfarmah.apps.readerapp.screens.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.components.UserForm
import com.kunalfarmah.apps.readerapp.screens.splash.ReaderLogo

@Preview
@Composable
fun LoginScreen(navController: NavController = NavController(App.context)) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReaderLogo()
            UserForm(loading = false, isCreateAccount = false){
                email, password ->
                Log.d("LOGIN", "LoginScreen: $email, $password")
            }
        }
    }
}

