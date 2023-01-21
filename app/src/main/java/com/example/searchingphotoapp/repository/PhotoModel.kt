package com.example.searchingphotoapp.repository

data class PhotoFeed(
    val items: MutableList<Photo>
)

data class Photo(
    val id: Int,
    val url: String,
    val photographer: String
)