package com.example.roadsideassistance.Common

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySplashAcreenBinding

class SplashAcreen : AppCompatActivity() {
    lateinit var binding: ActivitySplashAcreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashAcreenBinding.inflate(layoutInflater)
        window.statusBarColor = resources.getColor(R.color.btncolor)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this,Login::class.java))
            finish()
        }, 3000)
    }
}
