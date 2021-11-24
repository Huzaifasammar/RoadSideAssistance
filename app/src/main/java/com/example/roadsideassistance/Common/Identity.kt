package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.Provider.SignupProvider
import com.example.roadsideassistance.R
import com.example.roadsideassistance.Users.SignupUser
import com.example.roadsideassistance.databinding.ActivityIdentityBinding

class Identity : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    lateinit var binding: ActivityIdentityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivityIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.llServiceProvider.setOnClickListener(View.OnClickListener {
          startActivity(Intent(this,SignupProvider::class.java))
            finish()
        })
        binding.llUser.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,SignupUser::class.java))
            finish()
        })
    }
}