package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roadsideassistance.Provider.Provider_Signup
import com.example.roadsideassistance.R
import com.example.roadsideassistance.Users.User_Signup
import com.example.roadsideassistance.databinding.ActivityIdentityBinding

class Identity : AppCompatActivity() {
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    lateinit var binding: ActivityIdentityBinding
    var phone:String="";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivityIdentityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phone=intent.getStringExtra("number").toString();
        binding.llServiceProvider.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Provider_Signup::class.java)
            intent.putExtra("number",phone )
            startActivity(intent)
            finish()
        })
        binding.llUser.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, User_Signup::class.java)
            intent.putExtra("number",phone )
            startActivity(intent)
            finish()
        })
    }
}