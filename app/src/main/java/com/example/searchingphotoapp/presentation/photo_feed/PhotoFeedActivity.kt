package com.example.searchingphotoapp.presentation.photo_feed

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.searchingphotoapp.databinding.ActivityPhotoFeedBinding

class PhotoFeedActivity: Activity() {

    private lateinit var binding: ActivityPhotoFeedBinding
    private lateinit var presenter: PhotoFeedPresenterInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoFeedBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        presenter = PhotoFeedPresenter()
    }
}