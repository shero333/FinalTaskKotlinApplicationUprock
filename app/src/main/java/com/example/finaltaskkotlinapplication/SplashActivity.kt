package com.example.finaltaskkotlinapplication

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.finaltaskkotlinapplication.main.MainActivity
import com.google.firebase.FirebaseApp
import java.time.Duration
import java.time.Instant


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this@SplashActivity)

        val splashScreen: SplashScreen = installSplashScreen()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // Add a callback that's called when the splash screen is animating to
//        // the app content.
//        splashScreen.setOnExitAnimationListener { splashScreenView ->
//            // Create your custom animation.
//            val slideUp = ObjectAnimator.ofInt(splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat())
//
//            slideUp.interpolator = AnticipateInterpolator()
//            slideUp.duration = 200L
//
//            // Call SplashScreenView.remove at the end of your custom animation.
//            slideUp.doOnEnd { splashScreenView.remove() }
//
//            // Run your animation.
//            slideUp.start()
//        }

        setContentView(R.layout.activity_splash)

        splashScreen.setKeepOnScreenCondition { true }

        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        finish()

    }
}