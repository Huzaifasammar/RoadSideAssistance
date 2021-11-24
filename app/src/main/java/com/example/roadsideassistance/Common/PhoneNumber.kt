package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityPhoneNumberBinding

class PhoneNumber : AppCompatActivity() {
    lateinit var binding: ActivityPhoneNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.PhoneBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,PutOtp::class.java))
            finish()
        })
    }
}