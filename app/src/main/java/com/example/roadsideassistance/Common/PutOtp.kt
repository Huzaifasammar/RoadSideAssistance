package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityPutOtpBinding

class PutOtp : AppCompatActivity() {
    lateinit var binding: ActivityPutOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPutOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.verifyNumberBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Identity::class.java))
            finish()
        })
    }
}