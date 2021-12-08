package com.example.roadsideassistance.Users

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.Common.Identity
import com.example.roadsideassistance.Common.Login
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySignupUserBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class SignupUser : AppCompatActivity() {
    val RESULT_LOAD_IMAGE = 0
    lateinit var image:Uri
    lateinit var auth:FirebaseAuth
    lateinit var storage:StorageReference
    lateinit var reference:DatabaseReference
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
        storage=FirebaseStorage.getInstance().getReference().child("UserImages");
        auth= FirebaseAuth.getInstance()
        reference=FirebaseDatabase.getInstance().getReference()
        binding.tvLogin.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this,Login::class.java))
            finish()
        })
        binding.SignupBtn.setOnClickListener(View.OnClickListener {
            savedata()
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
    fun savedata() {
        auth.createUserWithEmailAndPassword(binding.emailSignup.toString().trim(), binding.passwordSignup.toString().trim())
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    val id = Objects.requireNonNull(task.result.user)!!.uid
                    val uniqueString = UUID.randomUUID().toString()
                    val Imagename: StorageReference = storage.child(uniqueString)
                    try {
                        Imagename.putFile(image).addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Select profile picture",
                                Toast.LENGTH_SHORT
                            ).show()


                            reference.child("User").child("Profile").child(id)
                                .setValue("huzaifa")
                        }.addOnSuccessListener {
                            Imagename.downloadUrl.addOnSuccessListener { uri ->

                                reference.child("Users")
                                    .child(id).setValue("")
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                        }
                    } catch (e: DatabaseException) {
                        println("Error$e")
                    }
                }
            }).addOnFailureListener(OnFailureListener {
                Toast.makeText(
                    applicationContext,
                    "Error Signing up",
                    Toast.LENGTH_SHORT
                ).show()
            })
    }
}