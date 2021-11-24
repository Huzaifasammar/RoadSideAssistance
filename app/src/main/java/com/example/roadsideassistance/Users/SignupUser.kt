package com.example.roadsideassistance.Users

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.Common.Identity
import com.example.roadsideassistance.Common.Login
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySignupUserBinding

class SignupUser : AppCompatActivity() {
    lateinit var binding: ActivitySignupUserBinding
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, Identity::class.java))
        finish()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivitySignupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        })
    }
}