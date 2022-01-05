package com.example.roadsideassistance.Adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roadsideassistance.Models.OrderModel;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.Models.UserModel;
import com.example.roadsideassistance.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ProviderDetailsAdapter extends RecyclerView.Adapter<ProviderDetailsAdapter.ViewHolder> {
    Context context;
    ArrayList<ProviderModel> list;
    String service;
    FirebaseUser user;
    String userName,user_Image,phone_number,user_address;

    public ProviderDetailsAdapter(Context context, ArrayList<ProviderModel> list,String service, String user_address) {
        this.context = context;
        this.list = list;
        this.service=service;
        this.user_address=user_address;
    }

    @NonNull
    @NotNull
    @Override
    public ProviderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.provider_detail_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProviderDetailsAdapter.ViewHolder holder, int position) {
        ProviderModel model=list.get(position);
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        user=FirebaseAuth.getInstance().getCurrentUser();
        reference.child("Users").child("Profile").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    userName=userModel.getUsername();
                    user_Image=userModel.getImageURL();
                    phone_number=userModel.getPhone_number();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        if(model!=null)
        {
            Glide.with(context).load(model.getImageURL()).into(holder.providerImage);
            if(!model.getReviews().equals(""))
            {
                holder.review.setText(model.getReviews());
            }
            holder.address.setText(model.getAddress());
            holder.providerName.setText(model.getUsername());
            holder.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + model.getPhone_number()));
                    if (model.getPhone_number()!=null) {
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        Toast.makeText(context, "Captain don't have mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
       if(service.equals("Mechanic"))
       {
           holder.mainLayout.setBackground(context.getResources().getDrawable(R.color.light_blue));
       }
       if(service.equals("Electrician"))
       {
           holder.mainLayout.setBackground(context.getResources().getDrawable(R.color.light_orange));

       }
        if(service.equals("Tyre Services"))
        {
            holder.mainLayout.setBackground(context.getResources().getDrawable(R.color.light_green));

        }
        if(service.equals("Fuel Supply"))
        {
            holder.mainLayout.setBackground(context.getResources().getDrawable(R.color.light_red));

        }
        if(service.equals("Tow Chain"))
        {
            holder.mainLayout.setBackground(context.getResources().getDrawable(R.color.light_gray));

        }
        holder.service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

                String order_id= String.valueOf(System.currentTimeMillis());
                OrderModel orderModel=new OrderModel(order_id,"new",user.getUid(),model.getId(),model.getUsername(),model.getPhone_number(),model.getAddress(),model.getImageURL(),userName,user_Image,phone_number,user_address);
                reference.child("Orders").child(service).child(order_id).setValue(orderModel).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context,"Order Boocked",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView providerName,review,address;
        CircularImageView providerImage;
        AppCompatButton service;
        RelativeLayout mainLayout;
        ImageView call;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            providerName=itemView.findViewById(R.id.provider_name);
            service=itemView.findViewById(R.id.service);
            address=itemView.findViewById(R.id.address);
            review=itemView.findViewById(R.id.provider_review);
            call=itemView.findViewById(R.id.telephone);
            providerImage=itemView.findViewById(R.id.provider_image);
            mainLayout=itemView.findViewById(R.id.Rl_sample);
        }
    }
    public void filterlist(ArrayList<ProviderModel> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }

}
