package com.example.roadsideassistance.Users;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.roadsideassistance.Common.SignIn;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.Models.UserModel;
import com.example.roadsideassistance.Provider.Provider_Signup;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityUserSignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class User_Signup extends AppCompatActivity {
    ActivityUserSignupBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    String device_token;
    String TAG="USER_SIGNUP";
    Uri image;
    ProgressDialog dialog;
    StorageReference storage;
    final int RESULT_LOAD_IMAGE = 0;
    final static int REQUEST_CODE=101;
    String phoneNumber;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            assert data != null;
            image = data.getData();
            binding.image.setImageURI(image);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Creating Account!");
        dialog.setMessage("We are Creating your account");
        storage= FirebaseStorage.getInstance().getReference().child("UserImages");
        device_token= FirebaseMessaging.getInstance().getToken().toString();
        phoneNumber=getIntent().getStringExtra("number");

        binding.RLImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(User_Signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

                } else {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            }
        });
        binding.SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( !validateusername() || !validateemail() || !validatePassword()){
                    Log.d(TAG, "onClick: "+" Validation failed");
                }
                else if(image.equals(null))
                {
                    Toast.makeText(getApplicationContext(),"PLease Select Image",Toast.LENGTH_SHORT).show();
                }
                else {
                    savedata();
                }
            }
        });
    }
    public boolean validateusername() {
        String val =binding.nameSignup.getText().toString();
        String checkSpace = "\\A\\w{4,20}\\z";
        if (val.isEmpty()) {
            binding.nameSignup.setError("This field cannot be empty");
            return false;
        } else if (val.length() > 20) {
            binding.nameSignup.setError("Username is too large");
            return false;
        } else if (val.length() < 4) {
            binding.nameSignup.setError("Username must be 4 digits");
            return false;
        } else if (!val.matches(checkSpace)) {
            binding.nameSignup.setError("White Spaces are not allowed");
            return false;
        } else {
            binding.nameSignup.setError(null);
            binding.nameSignupLayout.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateemail() {
        String val =binding.emailSignup.getText().toString();
        String checkEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (val.isEmpty()) {
            binding.emailSignup.setError("This field cannot be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            binding.emailSignup.setError("Plese use email pattern abc@iiu.edu.pk!");
            return false;
        } else {
            binding.emailSignup.setError(null);
            binding.emailLayout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = binding.passwordSignup.getText().toString().trim();
        if (val.isEmpty()) {
            binding.passwordSignup.setError("This field cannot be empty");
            return false;
        }
        else if(val.length() < 6)
        {
            binding.passwordSignup.setError("Password Must be greter or equal to 6 character");
            return false;
        }
        else {
            binding.passwordSignup.setError(null);
            binding.passwordLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void savedata()
    {
        auth.createUserWithEmailAndPassword(binding.emailSignup.getText().toString().trim(),binding.passwordSignup.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    dialog.show();
                    String id= Objects.requireNonNull(task.getResult().getUser()).getUid();
                    String uniqueString = UUID.randomUUID().toString();
                    StorageReference Imagepath=storage.child(uniqueString);

                    try {
                        Imagepath.putFile(image).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {

                                Toast.makeText(getApplicationContext(), "Select profile picture", Toast.LENGTH_SHORT).show();
                                UserModel helper = new UserModel("",binding.nameSignup.getText().toString().trim(),binding.emailSignup.getText().toString().trim(),
                                        binding.passwordSignup.getText().toString().trim(),id,device_token,phoneNumber);
                                reference.child("Providers").child("Profile").child(id).setValue(helper);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        UserModel helper = new UserModel(uri.toString(),binding.nameSignup.getText().toString().trim(),binding.emailSignup.getText().toString().trim(),
                                                binding.passwordSignup.getText().toString().trim(),id,device_token,phoneNumber);
                                        reference.child("Users").child("Profile").child(id).setValue(helper);
                                        dialog.dismiss();
                                        startActivity(new Intent(User_Signup.this, SignIn.class));
                                        finish();
                                    }
                                });
                            }
                        });

                    } catch (DatabaseException e)
                    {
                        System.out.println("Error"+ e);
                    }
                }

            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error Signing up", Toast.LENGTH_SHORT).show();
            }
        });
    }

}