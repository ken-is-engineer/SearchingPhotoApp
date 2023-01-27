package com.example.searchingphotoapp.presentation.photo_feed

import android.content.Context
import com.example.searchingphotoapp.presentation.photo_detail.PhotoDetailBuilder
import com.example.searchingphotoapp.repository.Photo

interface PhotoFeedRouterInterface {
    fun showPhotoDetail(context: Context, photo: Photo)
}

class PhotoFeedRouter: PhotoFeedRouterInterface {
    override fun showPhotoDetail(context: Context, photo: Photo) {
        val intent = PhotoDetailBuilder().build(context, photo)
        context.startActivity(intent)
    }
}