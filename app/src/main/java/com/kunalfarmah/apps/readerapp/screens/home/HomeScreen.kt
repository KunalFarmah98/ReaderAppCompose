package com.kunalfarmah.apps.readerapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.CircularLoader
import com.kunalfarmah.apps.readerapp.components.FABContent
import com.kunalfarmah.apps.readerapp.components.ListCard
import com.kunalfarmah.apps.readerapp.components.TitleSection
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.nav.ScreenNames
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: BooksViewModel = hiltViewModel()
    Scaffold(
        topBar = { AppBar(title = " Reader Boi", navController = navController) },
        floatingActionButton = {
            FABContent {
                navController.navigate(ScreenNames.SearchScreen.name)
            }
        })
    {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            HomeContent(navController = navController, viewModel)
        }
    }
}


@Composable
fun HomeContent(navController: NavController, viewModel: BooksViewModel){
    val booksState = viewModel.allBooks.collectAsState().value
    val user = Firebase.auth.currentUser
    val currentUserName = if(user?.email?.isNullOrEmpty() == false)
        user!!.email!!.split('@')[0]
    else
        "N/A"
    Column(modifier = Modifier.padding(top=80.dp, start = 10.dp, end = 10.dp),
        verticalArrangement = Arrangement.Top) {
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
        if(booksState.loading == true){
            CircularLoader()
        }
        else {
            ReadingRightNowArea(books = booksState.data?.filter {
                it.userId == user?.uid && it.startedReading != null && it.finishedReading == null
            } ?: listOf(), navController = navController)
        }

        Spacer(modifier = Modifier.height(20.dp))        
        TitleSection(label = "Reading List")
        if(booksState.loading == true){
            CircularLoader()
        }
        else {
            BookListArea(listOfBooks = booksState.data?.filter {
                it.userId == user?.uid && it.startedReading == null && it.finishedReading == null
            } ?: listOf(), navController = navController)
        }
    }
}


@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController){
    HorizontalScrollableComponent(listOfBooks){
        Log.d("BOOKS", "BookListArea: $it")
        navController.navigate(ScreenNames.UpdateScreen.name + "/${it}")
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPressed: (String?)->Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(280.dp)
        .horizontalScroll(scrollState) ){
        for(book in listOfBooks){
            ListCard(book){
                onCardPressed(book.googleBookId)
            }
        }
    }
}


@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(books){
        Log.d("BOOKS", "BookListArea: $it")
        navController.navigate(ScreenNames.UpdateScreen.name+"/$it")
    }
}