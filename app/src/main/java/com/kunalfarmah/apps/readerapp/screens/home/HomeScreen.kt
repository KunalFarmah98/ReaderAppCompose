package com.kunalfarmah.apps.readerapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.nav.ScreenNames

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    Scaffold(
        topBar = { AppBar(title = " Reader Boi", navController = navController) },
        floatingActionButton = {
            FABContent {}
        })
    {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HomeContent(navController = navController)
        }
    }
}

@Composable
fun FABContent(onTap: () -> Unit) {
    FloatingActionButton(
        onClick = { onTap() }, shape = RoundedCornerShape(50.dp),
        containerColor = Color(0xFF92CBDF)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add a book",
            tint = Color.White
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String = "A Reader",
    showProfile: Boolean = true,
    navController: NavController = NavController(
        App.context
    )
) {
    TopAppBar(
        title = {
            Text(
                text = title, color = Color.Red.copy(0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
        },
        navigationIcon = {
            if (showProfile) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "App Icon",
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .size(25.dp)
                )
            }
        },
        actions = {
            IconButton(onClick = {
                Firebase.auth.signOut()
                navController.navigate(ScreenNames.LoginScreen.name)
            }) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Logout",
                    modifier = Modifier.size(25.dp)
                )
            }
        }

    )
}


@Composable
fun HomeContent(navController: NavController){
    val user = Firebase.auth.currentUser
    val currentUserName = if(user?.email?.isNullOrEmpty() == false)
        user!!.email!!.split('@')[0]
    else
         "N/A"
    Column(modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.SpaceEvenly) {
        Row(modifier = Modifier.align(alignment = Alignment.Start)){
            TitleSection(label = "Your reading \nactivity right now")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column {
                Icon(imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ScreenNames.StatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondaryContainer
                )
                Text(text = currentUserName,
                    modifier = Modifier.padding(2.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 15.sp,
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Divider()
            }
        }
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier, label: String) {
    Surface(modifier = modifier.padding(start = 5.dp, top= 1.dp)){
        Text(
            text = label,
            style = TextStyle(fontSize = 19.sp, fontStyle = FontStyle.Normal, textAlign = TextAlign.Left)
        )
    }
}



@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {

}