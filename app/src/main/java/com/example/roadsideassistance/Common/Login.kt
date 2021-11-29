package com.example.roadsideassistance.Common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.R
import com.example.roadsideassistance.Users.UserHome
import com.example.roadsideassistance.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    var TAG="Login"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        setContentView(binding.root)
        binding.signupTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Identity::class.java))
            finish()
        })
        binding.forgetTxt.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,ForgettPassword::class.java))
            finish()
        })
        binding.loginBtn.setOnClickListener(View.OnClickListener {
            if(!isValidEmail()||!isValidPassword())
            {
                Log.d(TAG, "onCreate: "+"Invalid Credentials")
            }
            else
            {
                startActivity(Intent(this,UserHome::class.java))
                finish()
            }
        })
        /*blockNotification= BlockNotification()
        statusBarNotification= StatusBarNotification(Parcel)
        blockNotification.onNotificationPosted(statusBarNotification)*/
    }



    fun isValidEmail():Boolean{
        val email:String=binding.emailLogin.text.toString()
        if(email.isEmpty())
        {
         binding.emailLogin.setError("email required")
            return false
        }
        else if (!email.matches(Regex(pattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+")))
            {
            binding.emailLogin.setError("invalid email")
                return false
            }
         else
        {
            binding.emailLogin.setError(null)
           return true
        }

    }
    fun isValidPassword():Boolean{
        val password:String=binding.passwordLogin.text.toString()
        if(password.isEmpty())
        {
            binding.passwordLogin.setError("password required")
            return false
        }
        else if(password.length<6)
        {
            binding.passwordLogin.setError("password must be greater or equal to 6 ")
            return false
        }
        else
        {
            binding.passwordLogin.setError(null)
            return true
        }
    }


}
