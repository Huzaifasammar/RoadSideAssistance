package com.example.roadsideassistance.Common;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.roadsideassistance.Provider.Provider_Home;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.Users.User_Home;
import com.example.roadsideassistance.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    DatabaseReference reference;
    FirebaseUser Currentuser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth=FirebaseAuth.getInstance();
        Currentuser=FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },3000);
    }
    public void checkUser()
    {
        if (Currentuser != null) {

            reference.child("Users").child("Profile").child(Currentuser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        startActivity(new Intent(SplashScreen.this, User_Home.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashScreen.this, Provider_Home.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
        }
        else
        {
            startActivity(new Intent(SplashScreen.this,SignIn.class));
            finish();
        }
    }
}