package com.kunalfarmah.apps.readerapp.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.screens.splash.ReaderLogo

@Preview
@Composable
fun LoginScreen(navController: NavController = NavController(App.context)) {
    val login = remember {
        mutableStateOf(true)
    }
    val btnText = if(login.value) "Login" else "Register"
    val optionText = if(login.value) "New Here? Register" else "Already A User? Login"

    Surface(modifier = Modifier.fillMaxSize()) {
        Column (verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.CenterHorizontally){
            ReaderLogo()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(modifier: Modifier){
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf(false)
    }
    val passwordFocusRequest = FocusRequester.Default
    val keyboardController = LocalSoftwareKeyboardController.current

    val valid = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    val modifier = Modifier
        .height(250.dp)
        .verticalScroll(rememberScrollState())
}