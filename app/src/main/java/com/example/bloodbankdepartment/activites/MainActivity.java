package com.example.bloodbankdepartment.activites;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.bloodbankdepartment.R;
import com.example.bloodbankdepartment.fragments.BloodRequest_Fragment;
import com.example.bloodbankdepartment.fragments.Home_Fragment;
import com.example.bloodbankdepartment.fragments.Profile_Fragment;
import com.example.bloodbankdepartment.fragments.SearchBloodDonor_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference bloodBankDepartment;
    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new Home_Fragment()).commit();
        }
    }
    private void initViews()
    {
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.chip_navigation_bar);
        frameLayout=findViewById(R.id.fragment_container);
        textView=findViewById(R.id.username_textview);
        bloodBankDepartment= FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentInfo");
    }
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener =
            item -> {
                Fragment selectedFragment = null;
                switch (item.getItemId()) {
                    case R.id.home_fragment:
                        selectedFragment = new Home_Fragment();
                        break;
                    case R.id.searchBloodDonor_fragment:
                        selectedFragment = new SearchBloodDonor_Fragment();
                        break;
                    case R.id.Notification_fragment:
                        selectedFragment = new BloodRequest_Fragment();
                        break;
                    case R.id.profile_fragment:
                        selectedFragment = new Profile_Fragment();
                        break;
                }
                assert selectedFragment != null;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        selectedFragment).commit();
                return true;
    };
    private void getUSerInformation(){
        String currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        bloodBankDepartment.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                String username=snapshot.child("fullname").getValue().toString();
                textView.setText("Welcome "+username);}
                else Toast.makeText(MainActivity.this,"Invalid User",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUSerInformation();
    }
}