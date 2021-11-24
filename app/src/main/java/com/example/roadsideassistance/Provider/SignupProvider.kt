package com.example.roadsideassistance.Provider

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.Common.Identity
import com.example.roadsideassistance.Common.Login
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySignupProvider2Binding

class SignupProvider : AppCompatActivity() {
    lateinit var binding: ActivitySignupProvider2Binding
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Identity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=resources.getColor(R.color.background)
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        binding= ActivitySignupProvider2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val services = arrayOf<String?>(
            "Mechanic",
            "Electrician",
            "Tyre Services",
            "Fuel Supply",
            "Tow Chain ",
            "Other",
        )
        val servicesAdapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.support_simple_spinner_dropdown_item, services)
         binding.selectservices.setAdapter<ArrayAdapter<*>>(servicesAdapter)

        binding.tvLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        })

    }
}