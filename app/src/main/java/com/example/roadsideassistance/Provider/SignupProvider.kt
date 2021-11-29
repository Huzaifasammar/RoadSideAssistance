package com.example.roadsideassistance.Provider

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.Common.Identity
import com.example.roadsideassistance.Common.Login
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySignupProvider2Binding

class SignupProvider : AppCompatActivity() {
    val RESULT_LOAD_IMAGE = 0
    lateinit var image:Uri
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
        binding.SignupBtn.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        })
        binding.RLImage.setOnClickListener(View.OnClickListener {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        })


    }
    fun isValidEmail():Boolean{
        val email:String=binding.emailSignup.text.toString()
        if(email.isEmpty())
        {
            binding.emailSignup.setError("email required")
            return false
        }
        else if (!email.matches(Regex(pattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+")))
        {
            binding.emailSignup.setError("invalid email")
            return false
        }
        else
        {
            binding.emailSignup.setError(null)
            return true
        }

    }
    fun isValidPassword():Boolean{
        val password:String=binding.passwordSignup.text.toString()
        if(password.isEmpty())
        {
            binding.passwordSignup.setError("password required")
            return false
        }
        else if(password.length<6)
        {
            binding.passwordSignup.setError("password must be greater or equal to 6 ")
            return false
        }
        else
        {
            binding.passwordSignup.setError(null)
            return true
        }
    }
    fun isvalidUsername():Boolean {
        val username=binding.nameSignup.text.toString()
        if(username.isEmpty())
        {
            binding.nameSignup.setError("username required")
            return false
        }
        else
        {
            binding.nameSignup.setError(null)
            return true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMAGE) {
            if (resultCode == RESULT_OK) {
                assert(data != null)
                image = data!!.data!!
                binding.image.setImageURI(image)
            }
        }
    }
}