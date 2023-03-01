package com.example.finaltaskkotlinapplication.utils

import androidx.paging.PagingSource
import com.example.finaltaskkotlinapplication.models.helperModels.UnsplashImageModelItem

interface IApiResponse<T> {
    fun onSuccess(response: UnsplashImageModelItem?, message: String?)
    fun onFailure(error: String?)
}