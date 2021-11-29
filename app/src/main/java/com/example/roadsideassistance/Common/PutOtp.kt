package com.example.roadsideassistance.Common

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.databinding.ActivityPutOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import java.util.*
import java.util.concurrent.TimeUnit

class PutOtp : AppCompatActivity() {
    var phone:String=""
    var count:Int=60
    var code:String=""
    var auth: FirebaseAuth? = null
    var Verification_id:String=""
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null
   lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var timer: Timer = java.util.Timer()
    var credential: PhoneAuthCredential? = null

    lateinit var binding: ActivityPutOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPutOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phone= intent.getStringExtra("phone").toString()
        val mask: String = phone.replace("\\w(?=\\w{4})".toRegex(), "*")
        binding.tvnumber.text = mask
        auth = FirebaseAuth.getInstance()
        callBack()
        sendCode()
        Timer()

        binding.verifyNumberBtn.setOnClickListener(View.OnClickListener {
            try {
                binding.spinKitVerify.visibility = View.VISIBLE
                binding.verifyNumberBtn.visibility = View.INVISIBLE
                credential = PhoneAuthProvider.getCredential(
                    Verification_id,
                    binding.pinView.text.toString()
                )
                sigincredential(credential!!)
            } catch (e: IllegalArgumentException) {
                binding.spinKitVerify.visibility = View.INVISIBLE
                binding.verifyNumberBtn.visibility = View.VISIBLE
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        })
        binding.ResendCode.setOnClickListener {
            Toast.makeText(applicationContext, "Code sent successfully", Toast.LENGTH_SHORT).show()
            resendVerificationCode(phone, resendToken!!)
        }
    }

    private fun sendCode() {
        val options: PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(mCallbacks!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    private fun Timer() {
        val delay = 1 // delay for 1 sec.
        val period = 1000
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val result: Int = --count
                    if (result == 0) {
                        timer.cancel()
                    } else {
                        binding.secondOtp.text = "00: $result"
                    }
                }
            }
        }, delay.toLong(), period.toLong())
    }

    private fun resendVerificationCode(
        phoneNumber: String,
        token: ForceResendingToken
    ) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks!!,
            token
        )
    }

    private fun callBack() {
        mCallbacks = object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                Verification_id = s
                resendToken = forceResendingToken
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                code = phoneAuthCredential.smsCode.toString()
            }

            override fun onVerificationFailed(e: FirebaseException) {
                binding.spinKitVerify.visibility = View.INVISIBLE
                binding.verifyNumberBtn.visibility = View.VISIBLE
                if (e is FirebaseAuthInvalidCredentialsException) {
                    binding.pinView.error = "Invalid phone number."
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun sigincredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, Identity::class.java)
                intent.putExtra("number",phone )
                startActivity(intent)
                finish()
            } else {
                binding.spinKitVerify.visibility = View.INVISIBLE
                binding.verifyNumberBtn.visibility = View.VISIBLE
                Toast.makeText(applicationContext, task.exception!!.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}