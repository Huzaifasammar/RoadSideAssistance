package com.example.roadsideassistance.Provider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.roadsideassistance.Adapters.OrdersAdapter;
import com.example.roadsideassistance.Models.OrderModel;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.R;
import com.example.roadsideassistance.databinding.ActivityOrderBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Order_Activity extends AppCompatActivity {
    ActivityOrderBinding binding;
    DatabaseReference reference,databaseReference;
    OrdersAdapter adapter;
    FirebaseUser user;
    final String TAG="ORDER_ACTIVITY";
    String service,toolbarHeading,status;
    LinearLayoutManager layoutManager;
    ArrayList<OrderModel> list=new ArrayList<>();

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Order_Activity.this,Provider_Home.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.spinKit.setVisibility(View.VISIBLE);
        toolbarHeading=getIntent().getStringExtra("heading");
        status=getIntent().getStringExtra("status");
        binding.toolbarName.setText(toolbarHeading);
        reference= FirebaseDatabase.getInstance().getReference();
        databaseReference=FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        layoutManager=new LinearLayoutManager(this);
        binding.recyclerviewOrder.setLayoutManager(layoutManager);

        binding.backBusTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Order_Activity.this,Provider_Home.class));
                finish();
            }
        });
        reference.child("Providers").child("Profile").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    ProviderModel providerModel=snapshot.getValue(ProviderModel.class);
                    service=providerModel.getService();
                    adapter=new OrdersAdapter(Order_Activity.this,list,status,service);
                    Handler handler=new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            databaseReference.child("Orders").child(service).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        for(DataSnapshot ds:snapshot.getChildren())
                                        {
                                            OrderModel model=ds.getValue(OrderModel.class);
                                            if(model.getStatus().equals(status))
                                            {
                                                list.add(model);
                                                binding.spinKit.setVisibility(View.INVISIBLE);
                                                binding.tvNoservice.setVisibility(View.INVISIBLE);
                                                binding.recyclerviewOrder.setAdapter(adapter);
                                            }

                                            else if(list.size()==0)
                                            {
                                                binding.tvNoservice.setVisibility(View.VISIBLE);
                                                binding.spinKit.setVisibility(View.INVISIBLE);
                                            }

                                        }
                                        adapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        binding.spinKit.setVisibility(View.INVISIBLE);
                                        binding.tvNoservice.setVisibility(View.VISIBLE);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    },2000);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }
}