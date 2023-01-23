package com.example.searchingphotoapp.repository

data class PhotoFeed(
    val photos: MutableList<Photo>
)

data class Photo(
    val id: Int,
    val src: Src,
    val photographer: String
)

data class Src(val original: String)