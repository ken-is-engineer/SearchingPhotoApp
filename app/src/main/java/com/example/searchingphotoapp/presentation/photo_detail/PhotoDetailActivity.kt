package com.example.searchingphotoapp.presentation.photo_detail

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import coil.load
import com.example.searchingphotoapp.core.Keys
import com.example.searchingphotoapp.databinding.ActivityPhotoDetailBinding
import com.example.searchingphotoapp.repository.Photo

class PhotoDetailActivity : Activity() {

    private lateinit var binding: ActivityPhotoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Keys.photo, Photo::class.java)
        } else {
            intent.getSerializableExtra(Keys.photo) as? Photo
        }

        binding = ActivityPhotoDetailBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)
        binding.photoImage.load(photo?.src?.original)
    }
}