package com.kunalfarmah.apps.readerapp.screens.stats

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.BookRow
import com.kunalfarmah.apps.readerapp.components.CircularLoader
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsScreen(navController: NavController) {
    val viewModel: BooksViewModel = hiltViewModel()
    val user = FirebaseAuth.getInstance().currentUser
    val booksState = viewModel.allBooks.collectAsState().value
    var books: List<MBook>

    Scaffold(topBar = {
        AppBar(
            title = "Book Stats",
            backIcon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ){
            navController.popBackStack()
        }
    }) {
        Surface(modifier = Modifier.padding(it)) {
            if(booksState.loading == true){
                CircularLoader()
            }
            else {
                books = booksState.data?.filter { book ->
                    book.userId == user?.uid
                } ?: listOf()
                val readBooks = books.filter { book -> book.finishedReading != null }
                val readingBooks = books.filter { book -> book.startedReading != null && book.finishedReading == null }


                Column(modifier = Modifier.padding(10.dp)) {
                    Row {
                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .padding(2.dp)
                        ) {
                            Icon(imageVector = Icons.Sharp.Person, contentDescription = "icon")
                        }
                        Text(text = "Hi, ${user?.email.toString().split("@")[0].uppercase()}")
                    }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp), shape = CircleShape,
                        elevation = CardDefaults.cardElevation(5.dp)
                    ) {
                         Column(modifier = Modifier.padding(start = 25.dp, top= 4.dp, bottom=4.dp),
                            horizontalAlignment = Alignment.Start) {
                            Text("Your Stats")
                            Divider()
                            Text("You are reading: ${readingBooks.size} books")
                            Text("You have read: ${readBooks.size} books")
                        }
                    }
                    Divider()
                    LazyColumn(modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(), contentPadding = PaddingValues(16.dp)){
                        items(items = readBooks) { book ->
                            BookRow(book = book, navController = null)
                        }
                    }
                }
            }
        }
    }

}