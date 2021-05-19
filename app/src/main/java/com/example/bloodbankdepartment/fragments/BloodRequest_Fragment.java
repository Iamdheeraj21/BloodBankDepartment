package com.example.bloodbankdepartment.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bloodbankdepartment.R;
import com.example.bloodbankdepartment.classes.BloodRequest;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BloodRequest_Fragment extends Fragment
{
    RecyclerView recyclerView;
    DatabaseReference request_Ref;
    String currentUserID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_notification, container, false);
        intiViews(view);
        getTheAllBloodRequests();

        return view;
    }

    private void getTheAllBloodRequests()
    {
         FirebaseRecyclerOptions options=new FirebaseRecyclerOptions.Builder<BloodRequest>()
                .setQuery(request_Ref.child(currentUserID),BloodRequest.class).build();

        FirebaseRecyclerAdapter<BloodRequest,BloodRequestNotificationViewHolder> adapter=new FirebaseRecyclerAdapter<BloodRequest, BloodRequestNotificationViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BloodRequestNotificationViewHolder holder, int i, @NonNull BloodRequest model)
            {
                final String listRequestId=getRef(i).getKey();
                DatabaseReference reference=getRef(i).child("status").getRef();
                reference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists()){
                            String status=snapshot.getValue().toString();
                            if(status.equals("Active"))
                            {
                                assert listRequestId != null;
                                request_Ref.child(listRequestId).addValueEventListener(new ValueEventListener() {
                                    @SuppressLint("SetTextI18n")
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        if(snapshot.exists()) {
                                            String applicatio = snapshot.child("applicationo").getValue().toString();
                                            String name = snapshot.child("fullname").getValue().toString();
                                            String blood = snapshot.child("bloodgroupname").getValue().toString();
                                            //String imageurl = snapshot.child("imageurl").getValue().toString();
                                            String date = snapshot.child("date").getValue().toString();
                                            holder.applicationNo.setText("Application No :" + applicatio);
                                            holder.name.setText(name);
                                            holder.requirement.setText("Requirement :" + blood);
                                            holder.date.setText("Date :- " + date);

                                            holder.circleImageView.setImageResource(R.drawable.person);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(getContext(), "Invalid Information", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                holder.accept.setOnClickListener(v ->
                {
                    assert listRequestId != null;
                    request_Ref.child(currentUserID).child(listRequestId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                   request_Ref.child(currentUserID).child(listRequestId).removeValue().addOnCompleteListener(task -> Log.d("Request Completed","Request aproved"));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    request_Ref.child(listRequestId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String applicationo=snapshot.child("applicationo").getValue().toString();
                                String fullname=snapshot.child("fullname").getValue().toString();
                                String date=snapshot.child("date").getValue().toString();
                                String bloodgroup=snapshot.child("bloodgroupname").getValue().toString();
                                String phonenumber=snapshot.child("phonenumber").getValue().toString();
                                String status="Complete";
                                HashMap<String,Object> hashMap=new HashMap<>();
                                hashMap.put("applicationo",applicationo);
                                hashMap.put("fullname",fullname);
                                hashMap.put("date",date);
                                hashMap.put("phonenumber",phonenumber);
                                hashMap.put("status",status);
                                hashMap.put("bloodgroup",bloodgroup);
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("ApprovedRequest").child(listRequestId);
                                String refKey=databaseReference.push().getKey();
                                databaseReference.child(refKey).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if(task.isSuccessful())
                                        {
                                            DatabaseReference bloodGroupUnit=FirebaseDatabase.getInstance().getReference().child("BloodGroupUnit").child(bloodgroup);
                                            bloodGroupUnit.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if(snapshot.exists()){
                                                        String groupUnit=snapshot.getValue().toString();
                                                        int bloodUnit=Integer.parseInt(groupUnit);
                                                        bloodUnit--;
                                                        bloodGroupUnit.setValue(bloodUnit).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task)
                                                            {
                                                                if(task.isSuccessful()){
                                                                    request_Ref.child(listRequestId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if(task.isSuccessful()){
                                                                                Toast.makeText(getContext(), "Blood Request Approved...", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        });}
                                                }
                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error){
                                                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });

                holder.reject.setOnClickListener(v -> {
                    assert listRequestId != null;
                    request_Ref.child(currentUserID).child(listRequestId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                request_Ref.child(currentUserID).child(listRequestId).removeValue().addOnCompleteListener(task -> request_Ref.child(listRequestId).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot12) {
                                        if(snapshot12.exists()){
                                            request_Ref.child(listRequestId).removeValue().addOnCompleteListener(task1 -> Toast.makeText(getContext() ,"Request not approved...", Toast.LENGTH_SHORT).show());
                                        }else
                                            Toast.makeText(getContext(), "Info not available", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }));
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                });
            }
            @NonNull
            @Override
            public BloodRequestNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_request_notification,parent,false);
                return new BloodRequestNotificationViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    static class BloodRequestNotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,applicationNo,date,requirement;
        FloatingActionButton accept,reject;
        CircleImageView circleImageView;
        public BloodRequestNotificationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.blood_request_username);
            applicationNo=itemView.findViewById(R.id.application_id);
            date=itemView.findViewById(R.id.blood_request_date);
            requirement=itemView.findViewById(R.id.blood_request_requirement);
            circleImageView=itemView.findViewById(R.id.blood_requester_image);
            accept=itemView.findViewById(R.id.request_accept);
            reject=itemView.findViewById(R.id.request_reject);
        }
    }
    private void intiViews(View view)
    {
        recyclerView=view.findViewById(R.id.blood_requests);
        currentUserID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        request_Ref= FirebaseDatabase.getInstance().getReference().child("BloodRequest");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

    }
}