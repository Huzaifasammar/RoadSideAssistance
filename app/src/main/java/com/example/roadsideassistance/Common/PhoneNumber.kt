package com.example.roadsideassistance.Common

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityPhoneNumberBinding

class PhoneNumber : AppCompatActivity() {
    lateinit var binding: ActivityPhoneNumberBinding
    var phone=""
    val TAG="PHONE_NUMBER"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.PhoneBtn.setOnClickListener(View.OnClickListener {
            if(!isvlaidPhone())
            {
                Log.d(TAG, "onCreate: "+"Number Invalid")
            }
            else
            {
                val i=Intent(this, PutOtp::class.java)
                i.putExtra("phone",binding.countryCodePicker.selectedCountryCodeWithPlus+phone)
                startActivity(i)
                finish()
            }
        })


    }

    fun isvlaidPhone(): Boolean {
        phone=binding.EtNumber.text.toString()
        if(phone.isEmpty())
        {
            binding.EtNumber.setError("Number Required")
            return false
        }
        else if(phone.length>10)
        {
            binding.EtNumber.setError("invalid number")
            return false
        }
        else
        {
            binding.EtNumber.setError(null)
            return true
        }
    }
}