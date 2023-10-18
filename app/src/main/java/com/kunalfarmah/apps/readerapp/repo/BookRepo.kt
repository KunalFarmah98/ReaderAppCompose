package com.kunalfarmah.apps.readerapp.repo

import com.kunalfarmah.apps.readerapp.data.DataOrException
import com.kunalfarmah.apps.readerapp.model.BookResponse
import com.kunalfarmah.apps.readerapp.network.BooksApi
import javax.inject.Inject

class BookRepo @Inject constructor(private val api: BooksApi) {
    private val dataOrExceptionBooks = DataOrException<List<BookResponse.Item>, Boolean, Exception>()
    private val dataOrExceptionBookInfo = DataOrException<BookResponse.Item, Boolean, Exception>()


    suspend fun getBooks(query: String): DataOrException<List<BookResponse.Item>,Boolean, Exception> {
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
}