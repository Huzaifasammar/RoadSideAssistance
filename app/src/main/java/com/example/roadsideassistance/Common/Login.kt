package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(binding.root)
        binding.signupTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,PhoneNumber::class.java))
            finish()
        })
    }
}