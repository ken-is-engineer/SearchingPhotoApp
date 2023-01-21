package com.example.searchingphotoapp.presentation.photo_feed

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.searchingphotoapp.databinding.ListItemEmptyBinding
import com.example.searchingphotoapp.databinding.ListItemPhotoBinding
import com.example.searchingphotoapp.repository.Photo

class PhotoFeedAdapter(private val viewInput: PhotoFeedViewInput, val photoList: MutableList<Photo>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val context: Context = viewInput as Activity

    enum class PhotoFeedViewType(val value: Int) {
        EMPTY(1),
        PHOTO(2)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return if (photoList.isEmpty()) {
            PhotoFeedViewType.EMPTY.value
        } else {
            PhotoFeedViewType.PHOTO.value
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PhotoFeedViewType.PHOTO.value -> {
                val binding = ListItemPhotoBinding.inflate(LayoutInflater.from(context), parent, false)
                PhotoViewHolder(binding)
            }
            else -> {
                val binding = ListItemEmptyBinding.inflate(LayoutInflater.from(context), parent, false)
                EmptyViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotoViewHolder -> {
                val item = photoList[position]
                holder.binding.photoImage.load(item.url)
                holder.binding.nameText.text = item.photographer
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int {
        return if (photoList.isEmpty()) 1 else photoList.size
    }
}


class PhotoViewHolder(val binding: ListItemPhotoBinding): RecyclerView.ViewHolder(binding.root)
class EmptyViewHolder(binding: ListItemEmptyBinding): RecyclerView.ViewHolder(binding.root)

