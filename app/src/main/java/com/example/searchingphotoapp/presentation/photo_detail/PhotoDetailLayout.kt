package com.example.searchingphotoapp.presentation.photo_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchingphotoapp.R
import com.example.searchingphotoapp.repository.Photo

class PhotoDetailLayout {

    enum class EventType {
        CLOSE;
    }

    @Composable
    fun CloseButton(event: (type: EventType) -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_default))
        ) {
            OutlinedButton(
                colors = ButtonDefaults.outlinedButtonColors(
                    backgroundColor = colorResource(id = R.color.transparent)
                ),
                border = BorderStroke(dimensionResource(id = R.dimen.border_default), color = colorResource(id = R.color.text)),
                onClick = {
                    event(EventType.CLOSE)
                },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Text(
                    text = stringResource(R.string.close),
                    color = colorResource(id = R.color.text)
                )
            }
        }
    }

    @Composable
    fun PhotoImage(photo: Photo) {
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