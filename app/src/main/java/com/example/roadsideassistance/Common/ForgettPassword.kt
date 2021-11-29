package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityForgettPasswordBinding

class ForgettPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgettPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivityForgettPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSendCode.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,PutForgettOtp::class.java))
            finish()
        })
    }
}