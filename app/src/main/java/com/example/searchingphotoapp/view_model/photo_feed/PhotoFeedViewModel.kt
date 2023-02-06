package com.example.searchingphotoapp.view_model.photo_feed

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.searchingphotoapp.repository.Photo

class PhotosViewModel : ViewModel() {
    private val _photos =  mutableStateListOf<Photo>()
    val photos = _photos

    fun addAll(photoList: MutableList<Photo>) {
        _photos.addAll(photoList)
    }

    fun clear() {
        _photos.clear()
    }
}

class SearchWordViewModel : ViewModel() {
    private var _word = mutableStateOf("")
    val word = _word

    fun update(value: MutableState<String>) {
        _word = value
    }
}