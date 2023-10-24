package com.kunalfarmah.apps.readerapp.screens.update

import android.util.Log
import android.widget.RatingBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.Timestamp
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.CircularLoader
import com.kunalfarmah.apps.readerapp.components.InputField
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.SoftwareKeyboardController
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.kunalfarmah.apps.readerapp.components.RoundedButton
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookScreen(navController: NavController, bookId: String, viewModel: BooksViewModel = hiltViewModel()) {
    Scaffold(topBar = {
        AppBar(
            title = "Update Books",
            backIcon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ){
            navController.popBackStack()
        }
    }) {
        val bookInfo = viewModel.allBooks.collectAsState().value
        Surface (modifier = Modifier
            .padding(it)
            .fillMaxSize()){
            Column(modifier = Modifier.padding(3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                if(bookInfo.loading == true){
                    CircularLoader()
                }
                else{
                    UpdateBook(
                        book = bookInfo.data?.filter { it.googleBookId == bookId }?.get(0)!!,
                        navController = navController
                    )
                }
                
            }
        }

    }

}

@Composable
fun UpdateBook(book: MBook, navController: NavController){
    val noteState = remember {
        mutableStateOf(if(book.notes?.isEmpty() == true) "No Thoughts available" else book.notes.toString())
    }
    val startedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val rating = remember {
        mutableStateOf(book.rating ?: 0.0)
    }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(5.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .border(
                    width = 1.dp,
                    color = Color.LightGray,
                    shape = CircleShape.copy(CornerSize(70))
                ),
            shape = CircleShape.copy(CornerSize(70)),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                Color.White
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 60.dp)
                    .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .width(75.dp)
                        .height(150.dp),
                    painter = rememberImagePainter(data = book.photoUrl),
                    contentDescription = "book image",
                    contentScale = ContentScale.Fit
                )
                Column(
                    modifier = Modifier
                        .padding(start = 40.dp)
                        .width(200.dp), horizontalAlignment = Alignment.End
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = book.title ?: "Title",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${book.authors}",
                        fontSize = 15.sp
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "${book.publishedDate}",
                        fontSize = 15.sp
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(50.dp))

        InputField(valueState = noteState,
            labelId = "Enter your thoughts",
            isSingleLine = false,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .height(150.dp),
            trailingIcon = null,
            onAction = KeyboardActions {
                if (noteState.value.isEmpty()) {
                    return@KeyboardActions
                }
            })

        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = {
                if (!startedReading.value && book.startedReading == null) {
                    startedReading.value = true
                }

            }, enabled = book?.startedReading == null) {
                if (book.startedReading == null) {
                    if (startedReading.value) {
                        Text(
                            text = "Started Reading!",
                            Modifier.alpha(0.6f),
                            fontSize = 18.sp,
                            color = Color.Red.copy(0.5f)
                        )
                    } else {
                        Text(text = "Start Reading", fontSize = 18.sp)
                    }
                } else {
                    Text(text = "Started Reading on ${convertTime(book.startedReading!!)}", fontSize = 18.sp,)
                }
            }

            TextButton(onClick = {
                if (!isFinishedReading.value && book.finishedReading == null) {
                    isFinishedReading.value = true
                }

            }, enabled = book?.finishedReading == null) {
                if (book.finishedReading == null) {
                    if(!isFinishedReading.value){
                        Text(text = "Mark as Read", fontSize = 18.sp,)
                    }
                    else{
                        Text(text = "Finished Reading!",
                            Modifier.alpha(0.6f),
                            fontSize = 18.sp,
                            color = Color.Red.copy(0.5f))
                    }

                }
                else{
                    Text(text = "Finished on: ${convertTime(book.finishedReading!!)}", fontSize = 18.sp)
                }

            }
        }

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Rating", fontSize = 18.sp, modifier = Modifier.padding(bottom = 20.dp))
            RatingBar(
                value = rating.value.toFloat(),
                style = RatingBarStyle.Fill(),
                onValueChange = {
                    rating.value = it.toDouble()
                },
                onRatingChanged = {
                    Log.d("TAG", "onRatingChanged: $it")
                }
            )

        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            RoundedButton(label = "Update"){

            }
            RoundedButton(label = "Delete"){

            }

        }
    }
}


fun convertTime(time: Timestamp): String{
    return SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(time.toDate())
}

fun saveNote(){

}