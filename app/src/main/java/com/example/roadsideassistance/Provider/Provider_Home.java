package com.example.roadsideassistance.Provider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.roadsideassistance.Common.SignIn;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityProviderHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class Provider_Home extends AppCompatActivity {
    ActivityProviderHomeBinding binding;
    ActionBarDrawerToggle toggle;
    DatabaseReference reference;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    final String TAG="PROVIDER_HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.btncolor));
        binding=ActivityProviderHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(binding.toolbar);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
        binding.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));
        binding.drawer.addDrawerListener(toggle);
        currentUser=FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Providers").child("Profile").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ProviderModel model=snapshot.getValue(ProviderModel.class);
                    if(model!=null) {
                        Glide.with(Provider_Home.this).load(model.getImageURL()).into(binding.riderimage);
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
                startActivity(new Intent(Provider_Home.this, SignIn.class));
                finish();
            }
        });
        binding.btnCheckNewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Provider_Home.this,Order_Activity.class);
                i.putExtra("heading","New Request");
                i.putExtra("status","new");
                startActivity(i);
            }
        });
        binding.btnCheckActiveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Provider_Home.this,Order_Activity.class);
                i.putExtra("heading","Active Order");
                i.putExtra("status","active");
                startActivity(i);
            }
        });
        binding.btnpendingorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Provider_Home.this,Order_Activity.class);
                i.putExtra("heading","Pending Order");
                i.putExtra("status","pending");
                startActivity(i);
            }
        });
        binding.btnCompletedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Provider_Home.this,Order_Activity.class);
                i.putExtra("heading","Completed Order");
                i.putExtra("status","completed");
                startActivity(i);
            }
        });
    }
}