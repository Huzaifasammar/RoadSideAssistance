package com.example.roadsideassistance.Provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.roadsideassistance.Common.SignIn;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityProviderSignupBinding;
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

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.UUID;

public class Provider_Signup extends AppCompatActivity {
    ActivityProviderSignupBinding binding;
    DatabaseReference reference;
    FirebaseAuth auth;
    String device_token;
    String TAG="POVIDER_SIGNUP";
    Uri image;
    ProgressDialog dialog;
    StorageReference storage;
    final int RESULT_LOAD_IMAGE = 0;
    private static final int REQUEST_CODE = 101;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProviderSignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        reference= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);
        dialog.setTitle("Creating Account!");
        dialog.setMessage("We are Creating your account");
        storage=FirebaseStorage.getInstance().getReference().child("ProvidersImages");
        device_token= FirebaseMessaging.getInstance().getToken().toString();
        phoneNumber=getIntent().getStringExtra("number");
        String[] services = {"Mechanic",
                "Electrician",
                "Tyre Services",
                "Fuel Supply",
                "Tow Chain ",
                "Other",};
        ArrayAdapter servicesAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, services);
        binding.selectservices.setAdapter(servicesAdapter);
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Provider_Signup.this, SignIn.class));
                finish();
            }
        });
        binding.RLImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Provider_Signup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);

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
                else if(image.equals(""))
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
                                ProviderModel helper = new ProviderModel("",binding.nameSignup.getText().toString().trim(),binding.emailSignup.getText().toString().trim(),
                                        binding.passwordSignup.getText().toString().trim(),binding.selectservices.getText().toString(),id,device_token,phoneNumber,binding.addressSignup.getText().toString(),"");
                                reference.child("Providers").child("Profile").child(id).setValue(helper);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Imagepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        ProviderModel helper = new ProviderModel(uri.toString(),binding.nameSignup.getText().toString().trim(),binding.emailSignup.getText().toString().trim(),
                                                binding.passwordSignup.getText().toString().trim(),binding.selectservices.getText().toString(),id,device_token,phoneNumber,binding.addressSignup.getText().toString(),"");
                                        reference.child("Providers").child("Profile").child(id).setValue(helper);
                                        dialog.dismiss();
                                        startActivity(new Intent(Provider_Signup.this, SignIn.class));
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