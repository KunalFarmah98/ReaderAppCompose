package com.kunalfarmah.apps.readerapp.model

data class MBook(
    var id: String,
    var title: String?=null,
    var authors: String?=null,
    var notes: String?=null,
    var imageUrl: String?=null
)
