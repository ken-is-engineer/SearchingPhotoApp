package com.example.searchingphotoapp.repository

import com.example.searchingphotoapp.core.Constants
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

    override fun fetchPhotoFeed(searchWord: String, page: Int): Result<PhotoFeed> {
        return try {
        val builder = OkHttpClient.Builder()
            .connectTimeout(Constants.timeOut, TimeUnit.SECONDS)
            .writeTimeout(Constants.timeOut, TimeUnit.SECONDS)
            .readTimeout(Constants.timeOut, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        val client = builder.build()
        val url = URL("${Constants.baseUrl}+${Constants.searchApi}?query=$searchWord&per_page=${Constants.perPage}&page=$page")
        val request = Request.Builder().addHeader(Constants.apiAuthorization, Constants.apiKey).url(url).build()
        val response = client.newCall(request).execute()
        val json = JSONObject(response.body!!.string())
        Result.Success(Gson().fromJson(json.toString(), PhotoFeed::class.java))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}