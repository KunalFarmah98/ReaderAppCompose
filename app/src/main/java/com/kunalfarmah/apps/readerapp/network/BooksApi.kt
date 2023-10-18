package com.kunalfarmah.apps.readerapp.network

import com.kunalfarmah.apps.readerapp.model.BookResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface BooksApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String) : BookResponse

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String): BookResponse.Item


}