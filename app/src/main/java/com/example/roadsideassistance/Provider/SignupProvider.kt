package com.example.roadsideassistance.Provider

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.roadsideassistance.Common.Identity
import com.example.roadsideassistance.Common.Login
import com.example.roadsideassistance.Models.ProviderModel
import com.example.roadsideassistance.R
import com.example.roadsideassistance.databinding.ActivitySignupProvider2Binding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.messaging.FirebaseMessaging
//import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*

class SignupProvider : AppCompatActivity() {
    val RESULT_LOAD_IMAGE = 0
     var image:Uri= Uri.EMPTY
    val TAG="POVIDER_SIGNUP"
    lateinit var  dialog:ProgressDialog
    lateinit var binding: ActivitySignupProvider2Binding
    lateinit var imagereference:StorageReference
    lateinit var auth:FirebaseAuth
    lateinit var device_token:String
    lateinit var reference:DatabaseReference
    lateinit var storage:StorageReference

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
        dialog= ProgressDialog(this)
        dialog.setTitle("Creating Account!")
        dialog.setMessage("We are Creating your account")
        Firebase.initialize(this)
        auth= FirebaseAuth.getInstance()
        Log.d(TAG, "onCreate: "+auth);


         device_token = FirebaseMessaging.getInstance().token.toString()
         reference=FirebaseDatabase.getInstance().reference

         storage=FirebaseStorage.getInstance().getReference().child("ProvidersImages");
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
            if(!isvalidUsername()||!isValidEmail()||!isValidPassword())
            {
                Log.d(TAG, "onCreate: "+"validation error")
            }
            else if(image.toString().isEmpty() )
            {
                Toast.makeText(applicationContext,"Image not Selected",Toast.LENGTH_SHORT).show()
            }
            else
            {
                saveData()
            }
        })
        binding.RLImage.setOnClickListener(View.OnClickListener {
            val i = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(i, RESULT_LOAD_IMAGE)
        })


    }
    private fun saveData(){
        val email:String=binding.emailSignup.text.toString().trim()
        val password:String=binding.passwordSignup.text.toString().trim()
      auth.createUserWithEmailAndPassword(email,password)
          .addOnCompleteListener(OnCompleteListener { task ->
              if(task.isSuccessful)
              {
                  //dialog.show()
                  val id = Objects.requireNonNull(task.result.user)!!.uid
                  Log.d(TAG, "saveData: "+id)
                  val uniqueString = UUID.randomUUID().toString()
                  imagereference=storage.child(uniqueString)

                      imagereference.putFile(image)
                          .addOnSuccessListener {
                          OnSuccessListener<UploadTask.TaskSnapshot> {
                              Log.d(TAG, "saveData: " + " Success " + task)
                              val providerModel = ProviderModel(
                                  image.toString(),
                                  binding.nameSignup.text.toString().trim(),
                                  email,
                                  password,
                                  binding.selectservices.text.toString().trim(),
                                  id,
                                  device_token
                              )
                              Log.d(TAG, "saveData: " + " Success " + "")
                              reference.child("Providers").child(id).push().setValue(providerModel).addOnCompleteListener(
                                  OnCompleteListener { task ->
                                      if(task.isSuccessful)
                                      {
                                          dialog.dismiss()
                                          startActivity(Intent(this, Login::class.java))
                                          finish()
                                      }
                                      else
                                      {
                                          Toast.makeText(applicationContext,task.exception.toString(),Toast.LENGTH_SHORT).show();
                                      }

                                  })

                          }
                      }
                  }
              else
              {
                  dialog.dismiss()
                  Log.e("error",task.exception.toString())
                  Toast.makeText(applicationContext,"else"+task.exception,Toast.LENGTH_SHORT).show()
              }
    })
    }
    private fun isValidEmail():Boolean{
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
    private fun isValidPassword():Boolean{
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
    private fun isvalidUsername():Boolean {
        val username=binding.nameSignup.text.toString()
        if(username.isEmpty())
        {
            binding.nameSignup.setError("username required")
            return false
        }
        if(username.length>15)
        {
            binding.nameSignup.setError("username is too large")
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