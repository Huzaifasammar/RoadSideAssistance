package com.example.roadsideassistance.Users

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityUserHomeBinding

class UserHome : AppCompatActivity() {
    lateinit var binding: ActivityUserHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}