package com.example.roadsideassistance.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityTrackOrderBinding;

public class TrackOrderActivity extends AppCompatActivity {
    ActivityTrackOrderBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTrackOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}