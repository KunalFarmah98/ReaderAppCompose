package com.kunalfarmah.apps.readerapp.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun HomeScreen(navController: NavController){
    val user = Firebase.auth.currentUser
    Text(text = user?.uid ?: "user")
}