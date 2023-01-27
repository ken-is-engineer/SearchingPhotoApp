package com.example.searchingphotoapp.presentation.photo_detail

import android.content.Context
import android.content.Intent
import com.example.searchingphotoapp.core.Keys
import com.example.searchingphotoapp.repository.Photo

class PhotoDetailBuilder {
    fun build(context: Context, photo: Photo): Intent {
        val intent = Intent(context, PhotoDetailActivity::class.java)
        intent.putExtra(Keys.photo, photo)
        return intent
    }
}