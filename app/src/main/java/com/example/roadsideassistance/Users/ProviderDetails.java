package com.example.roadsideassistance.Users;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.roadsideassistance.Adapters.ProviderDetailsAdapter;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityProviderDetailsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.TargetOrBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProviderDetails extends AppCompatActivity implements LocationListener {
    ActivityProviderDetailsBinding binding;
    ProviderDetailsAdapter adapter;
    DatabaseReference reference;
    String service;
    ArrayList<ProviderModel> list=new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    LocationManager manager;
    Geocoder geocoder;
    List<Address> addresses;
    private final int mintime=1000;
    private final int distance=1;
    double latitude,longitude;
    String address;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProviderDetails.this,User_Home.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.btncolor));

        binding=ActivityProviderDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        geocoder=new Geocoder(this, Locale.getDefault());
        manager=(LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();
        getlocationupdates();
        binding.spinKit.setVisibility(View.VISIBLE);
        service=getIntent().getStringExtra("service");

        if(service!=null)
        {
            binding.toolbarName.setText(service);
        }
        binding.backBusTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProviderDetails.this,User_Home.class));
                finish();
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.recyclerview.setLayoutManager(layoutManager);
        reference= FirebaseDatabase.getInstance().getReference();
        binding.searchbus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());

            }
        });
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter=new ProviderDetailsAdapter(ProviderDetails.this,list,service,address);
                reference.child("Providers").child("Profile").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for(DataSnapshot ds:snapshot.getChildren())
                            {
                                ProviderModel model=ds.getValue(ProviderModel.class);
                                if(model.getService().equals(service))
                                {
                                    list.add(model);
                                    binding.spinKit.setVisibility(View.INVISIBLE);
                                    binding.tvNoservice.setVisibility(View.INVISIBLE);
                                    binding.recyclerview.setAdapter(adapter);
                                }

                                else if(list.size()==0)
                                {
                                    binding.tvNoservice.setVisibility(View.VISIBLE);
                                    binding.spinKit.setVisibility(View.INVISIBLE);
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        },2000);


    }
    private void  filter (String text)
    {
        ArrayList<ProviderModel> filtereddata=new ArrayList<>();
        adapter= new ProviderDetailsAdapter(ProviderDetails.this,list,service,address);
        binding.recyclerview.setAdapter(adapter);
        reference.child("Providers").child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                list.clear();
                binding.tvNoservice.setVisibility(View.INVISIBLE);
                for (DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    ProviderModel helper=dataSnapshot.getValue(ProviderModel.class);
                    if(helper.getAddress().toLowerCase().contains(text.toLowerCase()) && helper.getService().equals(service))
                    {
                        filtereddata.add(helper);
                        binding.spinKit.setVisibility(View.INVISIBLE);
                        binding.tvNoservice.setVisibility(View.INVISIBLE);
                        adapter.filterlist(filtereddata);
                    }
                   else if(list.size()==0)
                    {
                        binding.tvNoservice.setVisibility(View.VISIBLE);
                        binding.spinKit.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getlocationupdates();
                fetchLocation();

            }
        }
    }
    // fetch current location and set to text view --------------------------------------------------
    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latitude=location.getLatitude();
                    longitude=location.getLongitude();
                    try {
                        addresses=geocoder.getFromLocation(latitude,longitude,1);
                        address = addresses.get(0).getAddressLine(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    // Getting location updates in this activity its not require --------------------------------------
    @SuppressLint("MissingPermission")
    private void getlocationupdates() {
        if(manager!=null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, mintime, distance,this);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mintime, distance,this);
                }

                else

                {
                    Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(viewIntent);
                    this.recreate();
                }
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            }
        }
    }
}