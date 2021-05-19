package com.example.bloodbankdepartment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbankdepartment.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Fragment extends Fragment
{
    TextView email,name,mobilenumber;
    CircleImageView imageView;
    DatabaseReference bloodBankInfo;
    String currentUserId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        intiViews(view);
        return view;
    }

    private void intiViews(View view)
    {
        email=view.findViewById(R.id.textview_email);
        mobilenumber=view.findViewById(R.id.textview_mobile);
        name=view.findViewById(R.id.textview_name);
        imageView=view.findViewById(R.id.profile_photo);
        bloodBankInfo= FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentInfo");
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        getInformation();
    }
    private void getInformation(){
        bloodBankInfo.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageurl=snapshot.child("imageurl").getValue().toString();
                    String fullname=snapshot.child("fullname").getValue().toString();
                    String bankemail=snapshot.child("email").getValue().toString();
                    String mobile=snapshot.child("mobilenumber").getValue().toString();

                    name.setText(fullname);
                    email.setText(bankemail);
                    mobilenumber.setText(mobile);
                    if(imageurl.equals("default")){
                        imageView.setImageResource(R.drawable.bloodbank);
                    }
                }else{
                    Toast.makeText(getContext(), "Information not available...", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}