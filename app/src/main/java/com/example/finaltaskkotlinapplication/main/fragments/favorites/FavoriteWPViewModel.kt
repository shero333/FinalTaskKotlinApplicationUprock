package com.example.finaltaskkotlinapplication.main.fragments.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finaltaskkotlinapplication.models.RoomImageModel
import com.example.finaltaskkotlinapplication.repository.room.RepositoryRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteWPViewModel @Inject constructor(private val repository : RepositoryRoom) : ViewModel() {

    private var favImagesMutableLiveData : MutableLiveData<List<RoomImageModel>> = MutableLiveData()

    fun getFavsList() : MutableLiveData<List<RoomImageModel>>{

        return favImagesMutableLiveData
    }

    fun getFavs(){
        viewModelScope.launch{
            favImagesMutableLiveData.postValue(repository.getAll())
        }
    }
}