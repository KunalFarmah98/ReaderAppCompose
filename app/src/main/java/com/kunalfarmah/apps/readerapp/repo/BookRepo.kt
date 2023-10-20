package com.kunalfarmah.apps.readerapp.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.kunalfarmah.apps.readerapp.data.DataOrException
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.model.MBook
import com.kunalfarmah.apps.readerapp.network.BooksApi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BookRepo @Inject constructor(private val api: BooksApi) {

    suspend fun getBooks(query: String): DataOrException<List<BookResponse.Item>,Boolean, Exception> {
        val dataOrExceptionBooks = DataOrException<List<BookResponse.Item>, Boolean, Exception>()
        try{
            dataOrExceptionBooks.loading = true
            dataOrExceptionBooks.data = api.getAllBooks(query).items
            if(dataOrExceptionBooks.data?.isNotEmpty() == true){
                dataOrExceptionBooks.loading = false
            }
        }
        catch (e:Exception){
            dataOrExceptionBooks.loading = false
            dataOrExceptionBooks.e = e
        }
        return dataOrExceptionBooks
    }


    suspend fun getBookInfo(bookId: String): DataOrException<BookResponse.Item,Boolean, Exception> {
        val dataOrExceptionBookInfo = DataOrException<BookResponse.Item, Boolean, Exception>()
        try{
            dataOrExceptionBookInfo.loading = true
            dataOrExceptionBookInfo.data = api.getBookInfo(bookId)
            if(dataOrExceptionBookInfo.data?.toString()?.isNotEmpty() == true){
                dataOrExceptionBookInfo.loading = false
            }
        }
        catch (e:Exception){
            dataOrExceptionBookInfo.loading = false
            dataOrExceptionBookInfo.e = e
        }
        return dataOrExceptionBookInfo
    }

    suspend fun getAllBooksFromDb(): DataOrException<List<MBook>, Boolean, Exception>{
        val dataOrException = DataOrException<List<MBook>, Boolean, Exception>()
        try{
            dataOrException.loading = true
            dataOrException.data = FirebaseFirestore.getInstance().collection("books").get().await().documents.map{
                it.toObject(MBook::class.java)!!
            }
        }
        catch (e: FirebaseFirestoreException){
            dataOrException.e = e
        }
        return dataOrException
    }
}