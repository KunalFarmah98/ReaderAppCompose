package com.kunalfarmah.apps.readerapp.screens.details

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kunalfarmah.apps.readerapp.components.AppBar
import com.kunalfarmah.apps.readerapp.components.CircularLoader
import com.kunalfarmah.apps.readerapp.components.RoundedButton
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.nav.ScreenNames
import com.kunalfarmah.apps.readerapp.viewmodel.BooksViewModel
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(navController: NavController, bookId: String) {
    val viewModel: BooksViewModel = hiltViewModel()

    LaunchedEffect(bookId, Dispatchers.IO) {
        viewModel.searchBook(bookId)
    }


    val bookState = viewModel.bookInfo.collectAsState().value
    Scaffold(topBar = {
        AppBar(
            title = "Book Details",
            backIcon = Icons.Default.ArrowBack,
            showProfile = false,
            navController = navController
        ) {
            navController.popBackStack()
        }
    }) {
        if (bookState.loading == true) {
            CircularLoader()
        } else if (bookState.data == null) {
            Text(text = "Error loading book. ${bookState.e?.message.toString()}")
        } else {
            val bookData = bookState.data?.volumeInfo
            Surface(
                modifier = Modifier
                    .padding(paddingValues = it)
                    .fillMaxSize()
            )
            {
                LazyColumn() {
                    item {
                        BookDetails(bookId, bookData, navController)
                    }
                }

            }
        }
    }
}

    fun cleanDesc(desc: String) =
        HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

    @Composable
    fun BookDetails(bookId: String, bookData: BookResponse.Item.VolumeInfo?, navController: NavController) {
        Column(
            modifier = Modifier.padding(top = 12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .width(125.dp)
                    .height(125.dp)
                    .padding(2.dp)
                    .border(width = 2.dp, color = Color.LightGray, shape = CircleShape),
                shape = CircleShape
            ) {
                Image(
                    painter = rememberImagePainter(data = bookData?.imageLinks?.thumbnail),
                    contentDescription = "book image",
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "${bookData?.title}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Authors: ${bookData?.authors}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Page Count: ${bookData?.pageCount.toString()}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Categories: ${bookData?.categories}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Published on: ${bookData?.publishedDate}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Publisher: ${bookData?.publisher}",
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                textAlign = TextAlign.Center
            )
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .border(width = 2.dp, color = Color.Gray)
                    .height((LocalConfiguration.current.screenHeightDp * 0.3f).dp)
                    .fillMaxWidth()
            ) {
                item {
                    Text(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        text = cleanDesc(bookData?.description.toString()),
                        fontSize = 15.sp
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                RoundedButton(label = "Save") {
                    val book = MBook(
                        title = bookData?.title,
                        authors = bookData?.authors.toString(),
                        photoUrl = bookData?.imageLinks?.thumbnail,
                        description = bookData?.description,
                        notes= "",
                        publishedDate = bookData?.publishedDate,
                        publisher = bookData?.publisher,
                        rating = 0.0,
                        pageCount = bookData?.pageCount.toString(),
                        caregories = bookData?.categories.toString(),
                        googleBookId = bookId,
                        userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
                        )
                    val db = FirebaseFirestore.getInstance()
                    val collection = db.collection("books")
                    if(book.toString().isNotEmpty()){
                        collection.add(book).addOnSuccessListener {
                            // update id field of the newly added book
                            val docID = it.id
                            collection.document(docID).update(hashMapOf("id" to docID) as Map<String,Any>)
                        }.addOnCompleteListener{
                            if(it.isSuccessful){
                                navController.navigate(ScreenNames.HomeScreen.name){
                                    popUpTo(0)
                                }
                            }
                        }.addOnFailureListener{
                            Log.e("BookDetailsScreen", "BookDetails: Error Updating book", it )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(25.dp))
                RoundedButton(label = "Cancel") {
                    navController.popBackStack()
                }
            }


        }

    }