package com.example.finaltaskkotlinapplication.main

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.AUTHORITY
import android.provider.MediaStore.getMediaUri
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.FileProvider
import androidx.core.graphics.toColor
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.databinding.ActivityMainBinding
import com.example.finaltaskkotlinapplication.main.fragments.adapters.ViewPagerAdapter
import com.example.finaltaskkotlinapplication.models.FirebaseImageModel
import com.example.finaltaskkotlinapplication.repository.api.Api
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URI
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var storage: StorageReference? = null
    private var database: DatabaseReference? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var uriClickedImage : Uri

    private val contract = registerForActivityResult(TakePicture()){
        uploadImage(uriClickedImage)
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //camera image
        val clickedImage = File(applicationContext.filesDir, "clicked_image.jpg")
        uriClickedImage = FileProvider.getUriForFile(applicationContext,"com.example.finaltaskkotlinapplication.fileProvider",clickedImage)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.icon_splash_background)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        binding.fab.setOnClickListener {

            val bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog.setContentView(R.layout.custom_dialog_layout)

            val camera = bottomSheetDialog.findViewById<AppCompatTextView>(R.id.camera_button)
            val album = bottomSheetDialog.findViewById<AppCompatTextView>(R.id.album_button)
            val cancel = bottomSheetDialog.findViewById<AppCompatButton>(R.id.cancel_button)

            camera!!.setOnClickListener {
                openCamera()
                bottomSheetDialog.dismiss()
            }

            album!!.setOnClickListener {
                openAlbum()
                bottomSheetDialog.dismiss()
            }

            cancel!!.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.show()
        }
    }

    private fun openCamera() {
        contract.launch(uriClickedImage)
    }

    @SuppressLint("IntentReset")
    private fun openAlbum() {

        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"
        getIntent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(getIntent, 100)

    }

    private fun uploadImage(imageUri: Uri?) {

        val filename = (Long.MAX_VALUE - System.currentTimeMillis()).toString()

        storage = FirebaseStorage.getInstance().getReference("images/$filename.jpg")
        database = FirebaseDatabase.getInstance().getReference("uploaded_images")

        if (imageUri != null) {

            progressDialog = ProgressDialog(this@MainActivity,R.style.AppCompatAlertDialogStyle)
            progressDialog.setTitle("Uploading image.....")
            progressDialog.show()

            storage!!.putFile(imageUri)
                .addOnSuccessListener { it ->

                    if (progressDialog.isShowing)
                        progressDialog.dismiss()

                    val snackbar = Snackbar.make(
                        binding.root, "Successfully Uploaded",
                        Snackbar.LENGTH_LONG
                    ).setAction("Ok", null)
                    snackbar.setActionTextColor(Color.BLUE)
                    val snackbarView = snackbar.view
                    snackbarView.setBackgroundColor(Color.LTGRAY)
                    val textView =
                        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.BLUE)
                    textView.textSize = 28f
                    snackbar.show()

                    //realtime database

                    it.storage.downloadUrl.addOnSuccessListener {
                        Log.d("URLFB", "uploadImage: $it")
                        database!!.child(filename)
                            .setValue(FirebaseImageModel(it.toString(), filename))
                    }

                }.addOnFailureListener {

                    if (progressDialog.isShowing)
                        progressDialog.dismiss()

                }.addOnProgressListener {
                    val percentage = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()
                    progressDialog.setMessage("Progress: $percentage%")
                }
        } else {
            Toast.makeText(this@MainActivity, "Unable to upload", Toast.LENGTH_SHORT).show()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //album image
        if (requestCode == 100 && data != null && data.data != null) {

            val imageUriAlbum = data.data
            uploadImage(imageUriAlbum)

            Log.d("AlbumImage", "onActivityResult: $imageUriAlbum")

        }
    }


}