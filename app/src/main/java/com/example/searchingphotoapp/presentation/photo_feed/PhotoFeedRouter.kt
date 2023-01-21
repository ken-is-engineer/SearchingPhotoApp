package com.example.searchingphotoapp.presentation.photo_feed

import android.content.Context

interface PhotoFeedRouterInterface {
    fun showDetail(context: Context, imageUrl: String)
}

class PhotoFeedRouter: PhotoFeedRouterInterface {
    override fun showDetail(context: Context, imageUrl: String) {
        TODO("Not yet implemented")
    }

}