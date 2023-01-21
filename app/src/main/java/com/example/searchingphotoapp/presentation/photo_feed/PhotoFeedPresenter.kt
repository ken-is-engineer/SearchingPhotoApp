package com.example.searchingphotoapp.presentation.photo_feed

interface PhotoFeedPresenterInterface {
    fun fetchFeed(isAdditional: Boolean = false)
}

class PhotoFeedPresenter(val viewInput: PhotoFeedViewInput): PhotoFeedPresenterInterface {
    override fun fetchFeed(isAdditional: Boolean) {
        viewInput.setPhotoFeed(mutableListOf(), isAdditional)
    }
}