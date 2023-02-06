package com.example.searchingphotoapp.presentation.photo_detail

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.searchingphotoapp.core.Keys
import com.example.searchingphotoapp.repository.Photo

class PhotoDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val photo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Keys.photo, Photo::class.java)
        } else {
            intent.getSerializableExtra(Keys.photo) as? Photo
        } ?: return

        setContent {
            ContainerLayout(photo)
        }
    }

    @Composable
    fun ContainerLayout(photo: Photo) {
        val layout = PhotoDetailLayout()
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            layout.CloseButton {
                when (it) {
                    PhotoDetailLayout.EventType.CLOSE -> {
                        finish()
                    }
                }
            }
            layout.PhotoImage(photo)
        }
    }
}