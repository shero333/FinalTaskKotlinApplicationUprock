package com.example.finaltaskkotlinapplication.utils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import coil.api.load
import com.bumptech.glide.Glide
import com.example.finaltaskkotlinapplication.R
import com.example.finaltaskkotlinapplication.databinding.ActivityFullViewBinding

class FullViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFullViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFullViewBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.icon_splash_background)

        binding.fullViewImage.load(intent.getStringExtra("image"))
//        Glide.with(this).load().into(binding.fullViewImage)
    }
}