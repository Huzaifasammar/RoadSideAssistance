package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityPutForgettOtpBinding

class PutForgettOtp : AppCompatActivity() {
    lateinit var binding: ActivityPutForgettOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPutForgettOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmitCode.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        })
    }
}