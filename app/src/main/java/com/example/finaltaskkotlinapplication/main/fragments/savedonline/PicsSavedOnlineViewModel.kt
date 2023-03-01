package com.example.finaltaskkotlinapplication.main.fragments.savedonline

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finaltaskkotlinapplication.models.FirebaseImageModel
import com.google.firebase.database.*

class PicsSavedOnlineViewModel : ViewModel() {
    private var imagesFirebaseLiveData: MutableLiveData<ArrayList<FirebaseImageModel>> =
        MutableLiveData()
    private var databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("uploaded_images")
    private lateinit var firebaseImages: ArrayList<FirebaseImageModel>

    fun getImages(): MutableLiveData<ArrayList<FirebaseImageModel>> {
        return imagesFirebaseLiveData
    }
    fun getFirebaseImages() {

        firebaseImages = ArrayList()

        databaseReference.keepSynced(true)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (snap in snapshot.children) {
                    Log.i("snapshot: ", "${snap.value}")
                    val obj = snap.getValue(FirebaseImageModel::class.java)!!
                    firebaseImages.add(FirebaseImageModel(obj.imageUrl, obj.name))

                }

                Log.d("FBImages", "getFirebaseImages: $firebaseImages")
                imagesFirebaseLiveData.value = firebaseImages

            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })

    }
}