package com.example.roadsideassistance.Common

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivityForgettPasswordBinding
import com.google.firebase.auth.FirebaseAuth


class ForgettPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgettPasswordBinding
    lateinit var auth:FirebaseAuth
    val TAG="Forget_passowrd";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivityForgettPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnSendCode.setOnClickListener(View.OnClickListener {
            resetpasswoord()
        })
    }
    private fun resetpasswoord() {
        val emailaddress: String =binding.verifyEmail.text.toString().trim();
        if(emailaddress.matches(Regex("[a-zA-Z0-9._-]+@[gmail]+.+[com]"))&&!emailaddress.isEmpty())
        {
            auth.sendPasswordResetEmail(emailaddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        Toast.makeText(applicationContext, "Check Your Email", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this,SignIn::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        else
        {
            binding.verifyEmail.setError("Email pattern not correct")
        }

    }
}