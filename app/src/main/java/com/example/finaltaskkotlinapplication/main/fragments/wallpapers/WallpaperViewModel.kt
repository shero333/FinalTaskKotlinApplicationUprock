package com.example.finaltaskkotlinapplication.main.fragments.wallpapers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.finaltaskkotlinapplication.main.fragments.wallpapers.pages.LoadPage
import com.example.finaltaskkotlinapplication.models.UnsplashImageModel
import com.example.finaltaskkotlinapplication.models.helperModels.UnsplashImageModelItem
import com.example.finaltaskkotlinapplication.repository.api.retroinjection.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(repository: UnsplashRepository) : ViewModel() {

    val list = repository.CallWallpaperApi().cachedIn(viewModelScope)

}