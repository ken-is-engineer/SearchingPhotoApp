package com.example.searchingphotoapp.repository

data class PhotoFeed(
    val photos: MutableList<Photo>
)

data class Photo(
    val id: Int,
    val src: Src,
    val photographer: String
): java.io.Serializable

data class Src(val original: String): java.io.Serializable