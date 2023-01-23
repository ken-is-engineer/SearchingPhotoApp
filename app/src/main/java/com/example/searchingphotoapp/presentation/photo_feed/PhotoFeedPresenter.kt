package com.example.searchingphotoapp.presentation.photo_feed

import com.example.searchingphotoapp.repository.PhotoRepository
import com.example.searchingphotoapp.repository.Result
import kotlinx.coroutines.*

interface PhotoFeedPresenterInterface {
    suspend fun fetchFeed(searchWord: String, isAdditional: Boolean = false)
}

class PhotoFeedPresenter(private val viewInput: PhotoFeedViewInput, private val photoRepository: PhotoRepository): PhotoFeedPresenterInterface {

    private var page: Int = 1

    override suspend fun fetchFeed(searchWord: String, isAdditional: Boolean) {
        if (isAdditional) page += 1
        val result = withContext(Dispatchers.Default) {
            photoRepository.fetchPhotoFeed(searchWord, page)
        }
        when (result) {
            is Result.Success -> {
                viewInput.setPhotoFeed(result.data.items, isAdditional)
            }
            is Result.Error -> {
                viewInput.showError(result.exception.message)
            }
        }
    }
}