package com.example.searchingphotoapp.presentation.photo_feed

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.layout.ContentScale
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
import com.example.searchingphotoapp.R
import com.example.searchingphotoapp.core.Constants
import com.example.searchingphotoapp.repository.Photo
import com.example.searchingphotoapp.repository.PhotoRepository
import com.example.searchingphotoapp.view_model.photo_feed.PhotoFeedViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface PhotoFeedViewInput {
    fun setPhotoFeed(photoList: MutableList<Photo>, isAdditional: Boolean)
    fun showPhotoDetail(photo: Photo)
    fun showError(message: String?)
}

class PhotoFeedActivity : ComponentActivity(), PhotoFeedViewInput {

    private lateinit var presenter: PhotoFeedPresenterInterface
    private lateinit var router: PhotoFeedRouterInterface
    private val viewModel = PhotoFeedViewModel()
    private var searchValue: String = ""

    //region - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PhotoFeedPresenter(this, PhotoRepository())
        router = PhotoFeedRouter()

        setContent {
            PhotoFeedLayout()
        }
    }

    //endregion


    //region - PhotoFeedViewInput

    override fun setPhotoFeed(photoList: MutableList<Photo>, isAdditional: Boolean) {
        if (!isAdditional) {
            viewModel.clear()
        }
        viewModel.addAll(photoList)
    }

    override fun showPhotoDetail(photo: Photo) {
        router.showPhotoDetail(this, photo)
    }

    override fun showError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    //endregion


    //region - private methods

    @OptIn(DelicateCoroutinesApi::class)
    private fun search(isAdditional: Boolean = false) {
        if (searchValue.isNotEmpty()) {
            GlobalScope.launch {
                presenter.fetchFeed(searchValue, isAdditional)
            }
        }
    }

    //endregion


    //region - compose

    @Composable
    fun PhotoFeedLayout() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            SearchLayout()
            GridListView(viewModel.photos)
        }
    }

    @Composable
    fun SearchLayout() {
        Row(
            modifier = Modifier
                .background(color = colorResource(id = R.color.background))
                .padding(dimensionResource(id = R.dimen.padding_wide))
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditText()
            SearchButton()
        }
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun EditText() {
        var text by remember { mutableStateOf("") }
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            value = text,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = colorResource(id = R.color.text),
                backgroundColor = colorResource(id = R.color.transparent),
                unfocusedBorderColor = colorResource(id = R.color.text),
                focusedBorderColor = colorResource(id = R.color.text),
                cursorColor = colorResource(id = R.color.text)
            ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.photo_feed_hint),
                color = colorResource(id = R.color.sub_text))
            },
            singleLine = true,
            onValueChange = { value ->
                searchValue = value
                text = value
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    search()
                }
            )
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    private fun SearchButton() {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = colorResource(id = R.color.transparent)
            ),
            border = BorderStroke(dimensionResource(id = R.dimen.border_wide), color = colorResource(id = R.color.text)),
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                search()
            }
        ) {
            Text(
                text = getString(R.string.search_button),
                color = colorResource(id = R.color.text)
            )
        }
    }

    @Composable
    fun GridListView(stateList: SnapshotStateList<Photo>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(color = colorResource(id = R.color.background))
                .fillMaxSize()
        ) {
            if (stateList.isNotEmpty()) {
                items(stateList) { photo ->
                    ListItem(photo)
                }
                if (stateList.size % Constants.perPage == 0) {
                    item {
                        PlusImage()
                    }
                }
            }
        }
    }

    @Composable
    private fun ListItem(item: Photo) {
        Column(
            Modifier
                .padding(dimensionResource(id = R.dimen.padding_default))
                .clickable { router.showPhotoDetail(this, item) }
        ) {
            BoxWithConstraints {
                val itemWidth = maxWidth - dimensionResource(id = R.dimen.padding_default)
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(item.src.small)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.photo_image_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 10))
                        .fillMaxWidth()
                        .height(itemWidth)
                )
            }
            Text(
                text = stringResource(id = R.string.photo_feed_photographer).format(item.photographer),
                style = TextStyle(fontSize = 16.sp, color = colorResource(id = R.color.text)),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun PlusImage() {
        BoxWithConstraints(
            Modifier
                .padding(dimensionResource(id = R.dimen.padding_default) * 6)
                .clickable { search(true) }
        ) {
            val itemWidth = maxWidth - dimensionResource(id = R.dimen.padding_default)
            Image(
                painter = painterResource(id = R.drawable.ic_plus),
                colorFilter = ColorFilter.tint(colorResource(id = R.color.content)),
                contentDescription = getString(R.string.photo_image_description),
                alignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemWidth)
            )
        }
    }

    //endregion
}