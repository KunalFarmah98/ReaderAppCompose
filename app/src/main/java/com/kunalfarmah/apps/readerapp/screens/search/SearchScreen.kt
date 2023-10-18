package com.kunalfarmah.apps.readerapp.screens.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.InputField
import com.kunalfarmah.apps.readerapp.data.DataOrException
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: BooksViewModel = hiltViewModel()){

    Scaffold(topBar = {
       AppBar(
           title = "Search Books",
           backIcon = Icons.Default.ArrowBack,
           navController = navController,
           showProfile = false
       ){
           navController.popBackStack()
       }
    }) {
        Surface (modifier = Modifier.padding(it)){
            Column {
                SearchForm(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    viewModel = viewModel){
                    query -> viewModel.searchBooks(query)
                }
                Spacer(modifier = Modifier.height(20.dp))
                BookList(navController, viewModel)
            }
        }

    }

}

@Composable
fun BookList(navController: NavController, viewModel: BooksViewModel){
    val booksState = viewModel.listOfBooks.collectAsState(initial = DataOrException(null,true,null)).value
    if(booksState.loading == true){
        CircularProgressIndicator()
    }
    else {
        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
            items(items = booksState.data ?: listOf()) { book ->
                BookRow(book, navController)
            }
        }
    }
}

@Composable
fun BookRow(book: BookResponse.Item, navController: NavController){
    Card(modifier = Modifier
        .clickable { }
        .fillMaxWidth()
        .height(100.dp)
        .padding(3.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 10.dp)) {
        Row(modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.Top){
            val imageUrl = "https://venturebeat.com/wp-content/uploads/2016/05/bf-1.jpg?w=1200&strip=all"
            Image(painter = rememberImagePainter(data=imageUrl),
                contentDescription = "book image",
                Modifier
                    .width(80.dp)
                    .fillMaxHeight(1f)
                    .padding(end = 10.dp))
            Column() {
                Text(text = book.volumeInfo?.title.toString(), overflow = TextOverflow.Ellipsis)
                Text(text = "Author: ${book.volumeInfo?.authors}", overflow = TextOverflow.Clip)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    hint: String = "search for books",
    viewModel: BooksViewModel,
    onSearch: (String)->Unit = {}
){
    Column() {
        val searchQueryState = rememberSaveable {
            mutableStateOf("")
        }
        val keyboardController = LocalSoftwareKeyboardController.current
        val valid = remember(searchQueryState.value){
            searchQueryState.value.trim().isNotEmpty()
        }

        InputField(valueState = searchQueryState, enabled = true, labelId = "Search",
            onAction = KeyboardActions{
                if(!valid){
                    return@KeyboardActions
                }
                onSearch(searchQueryState.value)
                keyboardController?.hide()


            },
            trailingIcon = null
        )
    }
}