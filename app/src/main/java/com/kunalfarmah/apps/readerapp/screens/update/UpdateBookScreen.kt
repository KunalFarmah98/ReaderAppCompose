package com.kunalfarmah.apps.readerapp.screens.update

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
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
import com.google.firebase.firestore.FirebaseFirestore
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.kunalfarmah.apps.readerapp.App
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.CircularLoader
import com.kunalfarmah.apps.readerapp.components.InputField
import com.kunalfarmah.apps.readerapp.components.RoundedButton
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.nav.ScreenNames
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookScreen(
    navController: NavController,
    bookId: String,
    viewModel: BooksViewModel = hiltViewModel()
) {
    Scaffold(topBar = {
        AppBar(
            title = "Update Books",
            backIcon = Icons.Default.ArrowBack,
            navController = navController,
            showProfile = false
        ) {
            navController.popBackStack()
        }
    }) {
        val bookInfo = viewModel.allBooks.collectAsState().value
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bookInfo.loading == true) {
                    CircularLoader()
                } else {
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
fun UpdateBook(book: MBook, navController: NavController) {
    val noteState = remember {
        mutableStateOf(if (book.notes?.isEmpty() == true) "No Thoughts available" else book.notes.toString())
    }
    val startedReading = remember {
        mutableStateOf(false)
    }

    val isFinishedReading = remember {
        mutableStateOf(false)
    }

    val openDialog = remember {
        mutableStateOf(false)
    }

    if (openDialog.value) {
        ShowAlertDialog(
            title = "Delete",
            content = "Are you sure that you want to delete ${book.title} from your collection?",
            openDialog = openDialog
        ) {
            FirebaseFirestore.getInstance().collection("books")
                .document(book.id!!).delete()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(
                            App.context,
                            "Successfully deleted book ${book.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                        // needed to recompose home screen
                        navController.navigate(ScreenNames.HomeScreen.name){
                            popUpTo(0)
                        }
                    } else {
                        Toast.makeText(
                            App.context,
                            "Error occurred while deleting book ${book.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(
                        App.context,
                        "Error occurred while deleting book ${book.title}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }


    val rating = remember {
        mutableStateOf(book.rating ?: 0.0)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
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
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(modifier = Modifier.width(180.dp), onClick = {
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
                    Text(
                        text = "Started on ${convertTime(book.startedReading!!)}",
                        fontSize = 16.sp,
                    )
                }
            }

            TextButton(modifier = Modifier.width(180.dp), onClick = {
                if (!isFinishedReading.value && book.finishedReading == null) {
                    isFinishedReading.value = true
                }

            }, enabled = book?.finishedReading == null) {
                if (book.finishedReading == null) {
                    if (!isFinishedReading.value) {
                        Text(text = "Mark as Read", fontSize = 18.sp)
                    } else {
                        Text(
                            text = "Finished Reading!",
                            Modifier.alpha(0.6f),
                            fontSize = 18.sp,
                            color = Color.Red.copy(0.5f)
                        )
                    }

                } else {
                    Text(
                        text = "Finished on ${convertTime(book.finishedReading!!)}",
                        fontSize = 16.sp
                    )
                }

            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            val changedNote = book.notes != noteState.value
            val changedRating = book.rating != rating.value
            val finishedReadingTs =
                if (isFinishedReading.value) Timestamp.now() else book.finishedReading
            val startedReadingTs =
                if (startedReading.value) Timestamp.now() else book.startedReading
            val shouldUpdate =
                changedNote || changedRating || startedReading.value || isFinishedReading.value
            val updatedBook = hashMapOf(
                "finished_reading" to finishedReadingTs,
                "started_reading" to startedReadingTs,
                "rating" to rating.value,
                "notes" to noteState.value
            ).toMap()

            RoundedButton(label = "Update") {

                if (shouldUpdate) {
                    FirebaseFirestore.getInstance()
                        .collection("books")
                        .document(book.id!!)
                        .update(updatedBook)
                        .addOnCompleteListener {
                            Log.d("UpdateBook", "UpdateBook: ${it.result.toString()}")
                            if (it.isSuccessful) {
                                Toast.makeText(
                                    App.context,
                                    "Successfully updated book ${book.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(
                                    App.context,
                                    "Error occurred while updating book ${book.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                App.context,
                                "Error occurred while updating book ${book.title}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("UpdateBook", "UpdateBook: Error updating document", it)
                        }
                }
            }
            RoundedButton(label = "Delete") {
                openDialog.value = true
            }
        }
    }
}

@Composable
fun ShowAlertDialog(
    title: String,
    content: String,
    openDialog: MutableState<Boolean>,
    onConfirm: () -> Unit
) {


    if (openDialog.value) {
        AlertDialog(
            title = { Text(title) },
            text = { Text(text = content) },
            onDismissRequest = { },
            confirmButton = {
                TextButton(onClick = {
                    onConfirm.invoke()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text("Go Back!")
                }
            },
        )
    }
}

fun convertTime(time: Timestamp): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm").format(time.toDate())
}