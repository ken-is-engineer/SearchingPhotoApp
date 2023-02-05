package com.example.searchingphotoapp.presentation.photo_feed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.searchingphotoapp.R
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
//    private val photoList: MutableState<MutableList<Photo>> = viewModel.photos

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
        if (isAdditional) {
//            val startPosition = (viewModel.photos.value?.size ?: 1) - 1
            viewModel.addAll(photoList)
//            adapter?.notifyItemInserted(startPosition)
        } else {
            viewModel.clear()
            viewModel.addAll(photoList)
//            adapter = PhotoFeedAdapter(this, this.photoList)
//            binding.photoFeedRecyclerView.layoutManager = if (photoList.isEmpty()) {
//                GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
//            } else {
//                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
//            }
//            binding.photoFeedRecyclerView.adapter = adapter
        }
    }

    override fun showPhotoDetail(photo: Photo) {
        router.showPhotoDetail(this, photo)
    }

    override fun showError(message: String?) {
//        message?.let {
//            Snackbar.make(binding.photoFeedRecyclerView,
//                it, Snackbar.LENGTH_SHORT)
//        }?.show()
    }

    //endregion


    //region - private methods

    @OptIn(DelicateCoroutinesApi::class)
    private fun search() {
        if (searchValue.isNotEmpty()) {
            GlobalScope.launch {
                presenter.fetchFeed(searchValue)
            }
        }
    }


    private val shapes = Shapes(
        small = RoundedCornerShape(6f),
        medium = RoundedCornerShape(6f),
        large = RoundedCornerShape(6f)
    )

    @Composable
    fun PhotoFeedLayout() {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            SearchLayout()
            GridListView(viewModel.photos)
        }
    }

    @Composable
    fun SearchLayout() {
        Row(
            modifier = Modifier
                .background(Color.Black)
                .padding(12.dp)
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
                textColor = Color.White,
                backgroundColor = Color.Black,
                placeholderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                focusedBorderColor = Color.Green,
                cursorColor = Color.Green
            ),
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
                backgroundColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, Color.LightGray),
            onClick = {
                keyboardController?.hide()
                focusManager.clearFocus()
                search()
            }
        ) {
            Text(
                text = getString(R.string.search_button),
                color = Color.White
            )
        }
    }


    @Composable
    fun GridListView(stateList: SnapshotStateList<Photo>) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            if (stateList.isNotEmpty()) {
                items(stateList) { photo ->
                    ListItem(photo)
                }
            }
        }
    }

    @Composable
    private fun ListItem(item: Photo) {
        val paddingDp = 8.dp
        Column(Modifier.padding(paddingDp)) {
            BoxWithConstraints {
                val itemWidth = maxWidth - paddingDp
                AsyncImage(
                    ImageRequest.Builder(LocalContext.current)
                        .data(item.src.small)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.photo_image_description),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 10))
                        .fillMaxWidth().height(itemWidth)
                )
            }
            Text(
                text = item.photographer,
                style = TextStyle(fontSize = 16.sp, color = Color.White),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Preview
    @Composable
    fun PreviewGridListView() {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            items(6) { photo ->
                PreviewListItem()
            }
        }
    }

    @Preview
    @Composable
    private fun PreviewListItem() {
        Column(Modifier.padding(8.dp)) {
            AsyncImage(
                ImageRequest.Builder(LocalContext.current)
                    .data("https://www.pexels.com/photo/black-jeep-wrangler-on-dirt-road-surrounded-by-trees-3248777/")
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.photo_image_description),
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RoundedCornerShape(percent = 10))
            )
            Text(
                text = "PreviewText",
                style = TextStyle(fontSize = 24.sp, color = Color.White),
                textAlign = TextAlign.Center
            )
        }
    }
    //endregion
}