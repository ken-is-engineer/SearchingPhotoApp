package com.example.searchingphotoapp.presentation.photo_feed

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.searchingphotoapp.repository.Photo
import com.example.searchingphotoapp.repository.PhotoRepository
import com.example.searchingphotoapp.view_model.photo_feed.PhotosViewModel
import com.example.searchingphotoapp.view_model.photo_feed.SearchWordViewModel
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
    private val photosViewModel = PhotosViewModel()
    private val searchWordViewModel = SearchWordViewModel()

    //region - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PhotoFeedPresenter(this, PhotoRepository())
        router = PhotoFeedRouter()

        setContent {
            ContainerLayout()
        }
    }

    //endregion


    //region - PhotoFeedViewInput

    override fun setPhotoFeed(photoList: MutableList<Photo>, isAdditional: Boolean) {
        if (!isAdditional) {
            photosViewModel.clear()
        }
        photosViewModel.addAll(photoList)
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
        val word = searchWordViewModel.word.value
        if (word.isNotEmpty()) {
            GlobalScope.launch {
                presenter.fetchFeed(word, isAdditional)
            }
        }
    }

    //endregion


    //region - compose

    @Composable
    fun ContainerLayout() {
        val layout = PhotoFeedLayout(searchWordViewModel, photosViewModel)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            layout.SearchLayout {event ->
                when (event) {
                    PhotoFeedLayout.EventType.SEARCH -> {
                        search()
                    }
                    else -> {}
                }
            }
            layout.GridListView(photosViewModel.photos) { event, item ->
                when(event) {
                    PhotoFeedLayout.EventType.LOAD_MORE -> {
                        search(true)
                    }
                    PhotoFeedLayout.EventType.SHOW_DETAIL -> {
                        router.showPhotoDetail(this@PhotoFeedActivity, item ?: return@GridListView)
                    }
                    else -> {}
                }
            }
        }
    }

    //endregion
}