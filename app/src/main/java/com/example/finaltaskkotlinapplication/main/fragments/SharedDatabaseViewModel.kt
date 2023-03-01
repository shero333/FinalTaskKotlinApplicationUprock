package com.example.finaltaskkotlinapplication.main.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finaltaskkotlinapplication.models.RoomImageModel
import com.example.finaltaskkotlinapplication.repository.room.RepositoryRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedDatabaseViewModel @Inject constructor(private val repository : RepositoryRoom): ViewModel() {

    fun LikeImage(imageUrl: String,id:Int,name: String){
        viewModelScope.launch {
            repository.insert(RoomImageModel(imageUrl,name,id))
        }
    }

    fun dislikeImage(roomImageModel:RoomImageModel){
        viewModelScope.launch {
            repository.delete(roomImageModel)
        }
    }

}