package com.kunalfarmah.apps.readerapp.di

import com.kunalfarmah.apps.readerapp.network.BooksApi
import com.kunalfarmah.apps.readerapp.repo.BookRepo
import com.kunalfarmah.apps.readerapp.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideBookRepository(api: BooksApi) = BookRepo(api)

    @Singleton
    @Provides
    fun provideBookApi(): BooksApi{
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(MoshiConverterFactory.create()).build().create(BooksApi::class.java)
    }
}