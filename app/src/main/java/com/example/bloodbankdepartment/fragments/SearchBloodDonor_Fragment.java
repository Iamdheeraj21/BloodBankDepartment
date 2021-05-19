package com.example.bloodbankdepartment.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bloodbankdepartment.R;
import com.example.bloodbankdepartment.classes.BloodDonor;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchBloodDonor_Fragment extends Fragment {

    EditText enterBloodGroup;
    FloatingActionButton search_button;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_blood_donor, container, false);
        intiViews(view);
        String bloodGroup=enterBloodGroup.getText().toString();
        if(!(bloodGroup.length() <=3)) Toast.makeText(getContext(), "Correct Bloodgroup?", Toast.LENGTH_SHORT).show();
        else if(!(bloodGroup.length() >=2)) Toast.makeText(getContext(), "Correct Bloodgroup?", Toast.LENGTH_SHORT).show();
        search_button.setOnClickListener(v -> {
            searchBloodDonors(bloodGroup);
        });
        return view;
    }

    private void searchBloodDonors(String bloodGroup)
    {
        FirebaseRecyclerOptions<BloodDonor> options=null;
        if(bloodGroup.equals("")){
            options=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                    .setQuery(databaseReference,BloodDonor.class)
                    .build();
        }
        if (!bloodGroup.equals("")){
            options=new FirebaseRecyclerOptions.Builder<BloodDonor>()
                    .setQuery(databaseReference.orderByChild("bloodgroup")
                    .startAt(bloodGroup)
                    .endAt(bloodGroup+"\uf8ff"),BloodDonor.class)
                    .build();
        }
        FirebaseRecyclerAdapter<BloodDonor,BloodDonorViewHolder> adapter=new FirebaseRecyclerAdapter<BloodDonor, BloodDonorViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BloodDonorViewHolder holder, int position, @NonNull BloodDonor model)
            {
                holder.textView_name.setText(model.getName());
                if(model.getImageurl().equals("")){
                    holder.circleImageView.setImageResource(R.drawable.person);
                }else {
                    Glide.with(Objects.requireNonNull(getContext())).load(model.getImageurl()).into(holder.circleImageView);
                }
            }
            @NonNull
            @Override
            public BloodDonorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_donor,parent,false);
                return new BloodDonorViewHolder(view);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class BloodDonorViewHolder extends RecyclerView.ViewHolder
    {
        TextView textView_name;
        CircleImageView circleImageView,visitButton;

        public BloodDonorViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_name=itemView.findViewById(R.id.bloodDonor_name);
            circleImageView=itemView.findViewById(R.id.bloodDonor_image);
            visitButton=itemView.findViewById(R.id.visit_button);
        }
    }

    private void intiViews(View view)
    {
        enterBloodGroup=view.findViewById(R.id.enter_bloodgroup);
        search_button=view.findViewById(R.id.serach_button);
        recyclerView=view.findViewById(R.id.blooddonor_recyclerview);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodDonor");
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
    }
}