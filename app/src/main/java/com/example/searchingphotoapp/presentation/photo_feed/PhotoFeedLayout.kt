package com.example.searchingphotoapp.presentation.photo_feed

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchingphotoapp.core.Constants
import com.example.searchingphotoapp.repository.Photo
import com.example.searchingphotoapp.view_model.photo_feed.PhotosViewModel
import com.example.searchingphotoapp.view_model.photo_feed.SearchWordViewModel


class PhotoFeedLayout(
    private val searchWordViewModel: SearchWordViewModel,
    private val photosViewModel: PhotosViewModel
) {

    enum class EventType {
        SEARCH,
        LOAD_MORE,
        SHOW_DETAIL;
    }

    //region - SearchLayout

    @Composable
    fun SearchLayout(event: (type: EventType) -> Unit) {
        Row(
            modifier = Modifier
                .background(color = colorResource(id = com.example.searchingphotoapp.R.color.background))
                .padding(dimensionResource(id = com.example.searchingphotoapp.R.dimen.padding_wide))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditText(event)
            SearchButton(event)
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun EditText(event: (type: EventType) -> Unit) {
        var text by remember { searchWordViewModel.word }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = searchWordViewModel.word.value,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = colorResource(id = com.example.searchingphotoapp.R.color.text),
                backgroundColor = colorResource(id = com.example.searchingphotoapp.R.color.transparent),
                unfocusedBorderColor = colorResource(id = com.example.searchingphotoapp.R.color.text),
                focusedBorderColor = colorResource(id = com.example.searchingphotoapp.R.color.text),
                cursorColor = colorResource(id = com.example.searchingphotoapp.R.color.text)
            ),
            placeholder = {
                Text(
                    text = stringResource(id = com.example.searchingphotoapp.R.string.photo_feed_hint),
                    color = colorResource(id = com.example.searchingphotoapp.R.color.sub_text)
                )
            },
            singleLine = true,
            onValueChange = { value ->
                text = value
                searchWordViewModel.update(mutableStateOf(value))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    event(EventType.SEARCH)
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SearchButton(event: (type: EventType) -> Unit) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = colorResource(id = com.example.searchingphotoapp.R.color.transparent)
            ),
            border = BorderStroke(dimensionResource(id = com.example.searchingphotoapp.R.dimen.border_wide), color = colorResource(id = com.example.searchingphotoapp.R.color.text)),
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                event(EventType.SEARCH)
            }
        ) {
            Text(
                text = stringResource(com.example.searchingphotoapp.R.string.search_button),
                color = colorResource(id = com.example.searchingphotoapp.R.color.text)
            )
        }
    }

    //endregion


    //region - GridListView

    @Composable
    fun GridListView(stateList: SnapshotStateList<Photo>, event: (type: EventType, item: Photo?) -> Unit) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(color = colorResource(id = com.example.searchingphotoapp.R.color.background))
                .fillMaxSize()
        ) {
            if (stateList.isNotEmpty()) {
                items(stateList) { photo ->
                    ListItem(photo, event)
                }
                if (stateList.size % Constants.perPage == 0) {
                    item {
                        PlusImage(event)
                    }
                }
            }
        }
    }

    @Composable
    private fun ListItem(item: Photo, event: (type: EventType, item: Photo) -> Unit) {
        Column(
            Modifier
                .padding(dimensionResource(id = com.example.searchingphotoapp.R.dimen.padding_default))
                .clickable { event(EventType.SHOW_DETAIL, item) }
        ) {
            BoxWithConstraints {
                val itemWidth = maxWidth - dimensionResource(id = com.example.searchingphotoapp.R.dimen.padding_default)
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(item.src.small)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(com.example.searchingphotoapp.R.string.photo_image_description),
                    contentScale = Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 10))
                        .fillMaxWidth()
                        .height(itemWidth)
                )
            }
            Text(
                text = stringResource(id = com.example.searchingphotoapp.R.string.photo_feed_photographer).format(item.photographer),
                style = TextStyle(fontSize = 16.sp, color = colorResource(id = com.example.searchingphotoapp.R.color.text)),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun PlusImage(event: (type: EventType, item: Photo?) -> Unit) {
        BoxWithConstraints(
            Modifier
                .padding(dimensionResource(id = com.example.searchingphotoapp.R.dimen.padding_default) * 6)
                .clickable { event(EventType.LOAD_MORE, null) }
        ) {
            val itemWidth = maxWidth - dimensionResource(id = com.example.searchingphotoapp.R.dimen.padding_default)
            Image(
                painter = painterResource(id = com.example.searchingphotoapp.R.drawable.ic_plus),
                colorFilter = ColorFilter.tint(colorResource(id = com.example.searchingphotoapp.R.color.content)),
                contentDescription = stringResource(com.example.searchingphotoapp.R.string.photo_image_description),
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemWidth)
            )
        }
    }

    //endregion
}