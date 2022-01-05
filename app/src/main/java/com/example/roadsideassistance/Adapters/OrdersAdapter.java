package com.example.roadsideassistance.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roadsideassistance.Models.OrderModel;
import com.example.roadsideassistance.Models.ProviderModel;
import com.example.roadsideassistance.Provider.Order_Activity;
import com.example.roadsideassistance.Provider.Provider_Home;
import com.example.roadsideassistance.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context context;
    ArrayList<OrderModel> list;
    String status,service;
    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();

    public OrdersAdapter(Context context, ArrayList<OrderModel> list,String status,String service) {
        this.context = context;
        this.list = list;
        this.status=status;
        this.service=service;
    }

    @NonNull
    @NotNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.order_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrdersAdapter.ViewHolder holder, int position) {
        OrderModel model=list.get(position);
        holder.username.setText(model.getUser_name());
        holder.address.setText(model.getUser_address());
        String ts = model.getOrder_id();
        long millisecond = Long.parseLong(ts);
        String datetimeString = DateFormat.format("MM-dd-yyyy hh:mm:ss a", new Date(millisecond)).toString();
        holder.time.setText(datetimeString);
       if (status.equals("new"))
       {
           holder.parent.setBackground(context.getResources().getDrawable(R.color.light_blue));
       }
        if(status.equals("active"))
        {
            holder.parent.setBackground(context.getResources().getDrawable(R.color.light_orange));
            holder.status.setText("Active");
            holder.status.setTextColor(context.getResources().getColor(R.color.teal_200));
            holder.reject.setVisibility(View.INVISIBLE);
            holder.accept.setVisibility(View.INVISIBLE);
            holder.track.setVisibility(View.VISIBLE);
            holder.track.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context.getApplicationContext(), "Tracking is Not available yet",Toast.LENGTH_SHORT).show();
                }
            });

        }
        if(status.equals("pending"))
        {
            holder.parent.setBackground(context.getResources().getDrawable(R.color.light_green));
            holder.track.setVisibility(View.INVISIBLE);
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
            holder.status.setText("Pending");
            holder.status.setTextColor(context.getResources().getColor(R.color.red));

        }
        if(status.equals("completed"))
        {
            holder.parent.setBackground(context.getResources().getDrawable(R.color.light_gray));
            holder.track.setVisibility(View.INVISIBLE);
            holder.accept.setVisibility(View.INVISIBLE);
            holder.reject.setVisibility(View.INVISIBLE);
            holder.status.setText("Completed");
            holder.status.setTextColor(context.getResources().getColor(R.color.teal_200));
        }
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.reject.getText().equals("Reject"))
                {
                    Toast.makeText(context.getApplicationContext(),"Order Rejected",Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.child("Orders").child(service).child(model.getOrder_id()).child("status").setValue("active");
                Toast.makeText(context.getApplicationContext(),"Order accepted please reach user as soon as possible!",Toast.LENGTH_SHORT).show();
                Intent i=(new Intent(context, Order_Activity.class));
                i.putExtra("status","active");
                i.putExtra("heading","Active Order");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView username,address,time,status;
        AppCompatButton accept,reject,track;
        ConstraintLayout parent;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.dialogname);
            address=itemView.findViewById(R.id.dialogaddres);
            accept=itemView.findViewById(R.id.btn_accept);
            reject=itemView.findViewById(R.id.btn_reject);
            track=itemView.findViewById(R.id.btn_track);
            parent=itemView.findViewById(R.id.order_parent);
            time=itemView.findViewById(R.id.time);
            status=itemView.findViewById(R.id.status);

        }
    }
}
