package com.example.searchingphotoapp.presentation.photo_detail

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchingphotoapp.R
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
            PhotoDetailLayout(photo)
        }
    }

    @Composable
    fun PhotoDetailLayout(photo: Photo) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CloseButton()
            PhotoImage(photo)
        }
    }

    @Composable
    private fun CloseButton() {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = Color.Transparent
                ),
                border = BorderStroke(1.dp, Color.LightGray),
                onClick = {
                    finish()
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = getString(R.string.close),
                    color = Color.White
                )
            }
        }
    }

    @Composable
    private fun PhotoImage(photo: Photo) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data(photo.src.original)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.photo_image_description),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}