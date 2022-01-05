package com.example.roadsideassistance.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.roadsideassistance.Common.SignIn;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.Provider.Provider_Home;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityUserHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class User_Home extends AppCompatActivity {
    ActivityUserHomeBinding binding;
    ActionBarDrawerToggle toggle;
    DatabaseReference reference;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    final String TAG="USER_HOME";

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.btncolor));
        binding=ActivityUserHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(binding.toolbar);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
        binding.drawer.addDrawerListener(toggle);
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Users").child("Profile").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ProviderModel model=snapshot.getValue(ProviderModel.class);
                    if(model!=null) {
                        Glide.with(User_Home.this).load(model.getImageURL()).into(binding.riderimage);
                        binding.ridername.setText(model.getUsername());
                        //Glide.with(Provider_Home.this).load(model.getImageURL()).into(binding.imageview);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(User_Home.this, SignIn.class));
                finish();
            }
        });
        binding.RlElectrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(User_Home.this,ProviderDetails.class);
                i.putExtra("service","Electrician");
                startActivity(i);
                finish();
            }
        });
        binding.RlFuelSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(User_Home.this,ProviderDetails.class);
                i.putExtra("service","Fuel Supply");
                startActivity(i);
                finish();
            }
        });
        binding.RlMechanic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(User_Home.this,ProviderDetails.class);
                i.putExtra("service","Mechanic");
                startActivity(i);
                finish();
            }
        });
        binding.RlTowChain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(User_Home.this,ProviderDetails.class);
                i.putExtra("service","Tow Chain");
                startActivity(i);
                finish();
            }
        });
        binding.RlTyreServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(User_Home.this,ProviderDetails.class);
                i.putExtra("service","Tyre Services");
                startActivity(i);
                finish();
            }
        });
    }
}