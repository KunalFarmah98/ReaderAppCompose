package com.kunalfarmah.apps.readerapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kunalfarmah.apps.readerapp.data.DataOrException
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.repo.BookRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val booksRepo: BookRepo) : ViewModel(){

    var listOfBooks: MutableStateFlow<DataOrException<List<BookResponse.Item>,Boolean,Exception>> =
        MutableStateFlow(DataOrException(null,true,null))

    var bookInfo: MutableStateFlow<DataOrException<BookResponse.Item,Boolean,Exception>> =
        MutableStateFlow(DataOrException(null,true,null))

    init {
        searchBooks("android")
    }

    public fun searchBooks(query: String){
        viewModelScope.launch {
            if(query.isEmpty())
                    return@launch
            listOfBooks.value.loading = true
            listOfBooks.value = booksRepo.getBooks(query)
            Log.d("BooksVM", "searchBooks: $query ->\n ${listOfBooks.value.data.toString()}")
            if(listOfBooks.value?.data.toString().isNotEmpty())
                listOfBooks.value.loading = false
        }
    }

    public fun searchBook(bookId: String){
        viewModelScope.launch {
            if(bookId.isEmpty())
                return@launch
            bookInfo.value.loading = true
            bookInfo.value = booksRepo.getBookInfo(bookId)
            Log.d("BooksVM", "searchBook: ${listOfBooks.value.data.toString()}")
            if(bookInfo.value?.data.toString().isNotEmpty()) {
                bookInfo.value.loading = false
            }
        }
    }
}