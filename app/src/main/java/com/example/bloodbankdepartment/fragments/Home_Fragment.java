package com.example.bloodbankdepartment.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bloodbankdepartment.activites.Add_Notification_Activity;
import com.example.bloodbankdepartment.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home_Fragment extends Fragment {
    TextView BG1, BG2, BG3, BG4, BG5, BG6, BG7, BG8;
    DatabaseReference bloodUnitRef;
    Button add_Notification;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_, container, false);
        initViews(view);
        getBloodUnitData();
        add_Notification.setOnClickListener(v ->{
            startActivity(new Intent(view.getContext(), Add_Notification_Activity.class));
        });
        return view;
    }

    private void initViews(View view) {
        bloodUnitRef = FirebaseDatabase.getInstance().getReference().child("BloodGroupUnit");
        BG1 = view.findViewById(R.id.bloodGroup_AP);
        BG2 = view.findViewById(R.id.bloodGroup_AN);
        BG3 = view.findViewById(R.id.bloodGroup_BP);
        BG4 = view.findViewById(R.id.bloodGroup_BN);
        BG5 = view.findViewById(R.id.bloodGroup_OP);
        BG6 = view.findViewById(R.id.bloodGroup_ON);
        BG7 = view.findViewById(R.id.bloodGroup_ABP);
        BG8 = view.findViewById(R.id.bloodGroup_ABN);
        add_Notification=view.findViewById(R.id.add_notification);
    }

    private void getBloodUnitData() {
        bloodUnitRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String AP = snapshot.child("A+").getValue().toString();
                    String AN = snapshot.child("A-").getValue().toString();
                    String BP = snapshot.child("B+").getValue().toString();
                    String BN = snapshot.child("B-").getValue().toString();
                    String OP = snapshot.child("O+").getValue().toString();
                    String ON = snapshot.child("O-").getValue().toString();
                    String ABP = snapshot.child("AB+").getValue().toString();
                    String ABN = snapshot.child("AB-").getValue().toString();

                    BG1.setText("A+ :" + AP);
                    BG2.setText("A- :" + AN);
                    BG3.setText("B+ :" + BP);
                    BG4.setText("B- :" + BN);
                    BG5.setText("O+ :" + OP);
                    BG6.setText("O- :" + ON);
                    BG7.setText("AB+ :" + ABP);
                    BG8.setText("AB- :" + ABN);
                }
            }
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}