package com.example.searchingphotoapp.view_model.photo_feed

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.searchingphotoapp.repository.Photo

class PhotoFeedViewModel : ViewModel() {
    private val _photos =  mutableStateListOf<Photo>()
    val photos = _photos

    fun addAll(photoList: MutableList<Photo>) {
        _photos.addAll(photoList)
    }

    fun clear() {
        _photos.clear()
    }
}