package com.example.searchingphotoapp.repository

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.net.URL
import java.util.concurrent.TimeUnit

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

interface PhotoRepositoryInterface {
    fun fetchPhotoFeed(searchWord: String, page: Int): Result<PhotoFeed>
}

class PhotoRepository : PhotoRepositoryInterface {

    private val apiKey = "OO9pl7ZYRCf7W59ZWjGRLQUPb6T5PZu9Ny56qCZiStMBm0KQWzdjMNAR"
    private val perPage = 15

    override fun fetchPhotoFeed(searchWord: String, page: Int): Result<PhotoFeed> {
        return try {
        val builder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        val client = builder.build()
        val url =
            URL("https://api.pexels.com/v1/search?query=$searchWord&per_page=$perPage&page=$page")
        val request = Request.Builder().addHeader("Authorization", apiKey).url(url).build()
        val response = client.newCall(request).execute()
        val json = JSONObject(response.body!!.string())
        Result.Success(Gson().fromJson(json.toString(), PhotoFeed::class.java))
        } catch (e: Exception) {
            Result.Error(e)
        }

    }
}