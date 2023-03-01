package com.example.finaltaskkotlinapplication.main.fragments.wallpapers.pages

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.finaltaskkotlinapplication.models.helperModels.UnsplashImageModelItem
import com.example.finaltaskkotlinapplication.repository.api.Api
import javax.inject.Inject

class LoadPage @Inject constructor(private val api: Api) : PagingSource<Int, UnsplashImageModelItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImageModelItem> {
        return try {

            val position = params.key ?: 1
            val response = api.getImages(position,30)

            Log.d("DataDirect", "load: $response")

            return LoadResult.Page(
                data = response,
                prevKey = if (position == 1) null else position.inc(),
                nextKey = if (response.size <30) null else position.inc()
            )


        } catch (e: java.lang.Exception) {

            LoadResult.Error(e)

        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashImageModelItem>): Int? {

        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }


}