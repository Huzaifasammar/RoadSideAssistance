package com.example.roadsideassistance.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.roadsideassistance.Provider.Provider_Home;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.Users.User_Home;
import com.example.roadsideassistance.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SignIn extends AppCompatActivity {
    ActivitySignInBinding binding;
    FirebaseDatabase database;
    String id;
    FirebaseAuth fAuth;
    FirebaseUser Currentuser;
    DatabaseReference dbreference;
    String TAG="SIGN_IN";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        dbreference = FirebaseDatabase.getInstance().getReference();
        Currentuser = FirebaseAuth.getInstance().getCurrentUser();
        binding.signupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,PhoneNumber.class));
                finish();
            }
        });
        binding.forgetTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this,ForgettPassword.class));
                finish();
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateemail()||!validatePassword())
                {
                    Log.d(TAG, "onCreate: "+"Invalid Credentials");
                }
                else
                {
                    Authentication();
                }
            }
        });
    }

    public boolean validateemail() {
        String val =binding.emailLogin.getText().toString();
        String checkEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (val.isEmpty()) {
            binding.emailLogin.setError("This field cannot be empty");
            return false;
        } else if (!val.matches(checkEmail)) {
            binding.emailLogin.setError("Plese use email pattern abc@iiu.edu.pk!");
            return false;
        } else {
            binding.emailLogin.setError(null);
            binding.emailLayout.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePassword() {
        String val = binding.passwordLogin.getText().toString().trim();
        if (val.isEmpty()) {
            binding.passwordLogin.setError("This field cannot be empty");
            return false;
        }
        else if(val.length() < 6)
        {
            binding.passwordLogin.setError("Password Must be greter or equal to 6 character");
            return false;
        }
        else {
            binding.passwordLogin.setError(null);
            binding.passwordLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void Authentication() {
        binding.spinKit.setVisibility(View.VISIBLE);
        binding.loginBtn.setVisibility(View.INVISIBLE);
        String Email = binding.emailLogin.getText().toString().trim();
       String Password = binding.passwordLogin.getText().toString().trim();
        fAuth.signInWithEmailAndPassword(Email, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                id = authResult.getUser().getUid();
                dbreference.child("Users").child("Profile").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            if (fAuth.getCurrentUser() != null) {
                                startActivity(new Intent(SignIn.this, User_Home.class));
                                finish();
                            }
                        } else {
                            if (fAuth.getCurrentUser() != null) {
                                startActivity(new Intent(SignIn.this, Provider_Home.class));
                                finish();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        binding.spinKit.setVisibility(View.INVISIBLE);
                        binding.loginBtn.setVisibility(View.VISIBLE);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                binding.spinKit.setVisibility(View.INVISIBLE);
                binding.loginBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}