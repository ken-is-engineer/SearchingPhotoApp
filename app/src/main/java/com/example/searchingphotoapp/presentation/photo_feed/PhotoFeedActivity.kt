package com.example.searchingphotoapp.presentation.photo_feed

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.searchingphotoapp.R
import com.example.searchingphotoapp.databinding.ActivityPhotoFeedBinding
import com.example.searchingphotoapp.repository.Photo

interface PhotoFeedViewInput {
    fun setPhotoFeed(photoList: MutableList<Photo>, isAdditional: Boolean)
}

class PhotoFeedActivity: Activity(), PhotoFeedViewInput {

    private lateinit var binding: ActivityPhotoFeedBinding
    private lateinit var presenter: PhotoFeedPresenterInterface
    private var adapter: PhotoFeedAdapter? = null
    private val photoList: MutableList<Photo> = mutableListOf()


    //region - Lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoFeedBinding.inflate(LayoutInflater.from(this), null, false)
        setContentView(binding.root)

        presenter = PhotoFeedPresenter(this)

        setupLayout()
        setCallbacks()
    }

    //endregion


    //region - PhotoFeedViewInput

    override fun setPhotoFeed(photoList: MutableList<Photo>, isAdditional: Boolean) {
        if (isAdditional) {
            val startPosition = this.photoList.size - 1
            this.photoList.addAll(photoList)
            adapter?.notifyItemRangeInserted(startPosition, this.photoList.size - 1)
        } else {
            this.photoList.clear()
            this.photoList.addAll(photoList)
            adapter = PhotoFeedAdapter(this, this.photoList)
            binding.photoFeedRecyclerView.layoutManager = if (photoList.isEmpty()) {
                GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
            } else {
                GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, true)
            }
            binding.photoFeedRecyclerView.adapter = adapter
        }
    }

    //endregion


    //region - private methods

    private fun setupLayout() {
        binding.root.setBackgroundColor(ContextCompat.getColor(this, R.color.background))
    }

    private fun setCallbacks() {
        binding.searchButton.setOnClickListener {
            presenter.fetchFeed()
            closeKeyboard()
        }

        binding.searchEditText.setOnEditorActionListener { textView, i, keyEvent ->
            val searchText = textView.text
            if (searchText.isNullOrBlank()) {
                false
            } else {
                presenter.fetchFeed()
                closeKeyboard()
                true
            }
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    //endregion
}