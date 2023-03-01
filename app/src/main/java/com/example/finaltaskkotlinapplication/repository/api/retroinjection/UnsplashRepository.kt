package com.example.finaltaskkotlinapplication.repository.api.retroinjection

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.finaltaskkotlinapplication.main.fragments.wallpapers.pages.LoadPage
import com.example.finaltaskkotlinapplication.repository.api.Api
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val api: Api) {
//    private val api: Api
//    init {
//        this.api = api
//    }

    fun CallWallpaperApi() = Pager(
        config = PagingConfig(pageSize = 30, maxSize = 100),
        pagingSourceFactory = { LoadPage(api) }
    ).liveData
}