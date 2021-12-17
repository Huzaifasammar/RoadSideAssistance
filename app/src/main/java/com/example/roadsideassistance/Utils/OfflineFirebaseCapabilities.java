package com.example.roadsideassistance.Utils;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

    public class OfflineFirebaseCapabilities extends Application {
        @Override
        public void onCreate() {
            super.onCreate();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
    }
