package com.example.bloodbankdepartment.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bloodbankdepartment.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Add_Notification_Activity extends AppCompatActivity {
    TextInputEditText description;
    EditText title,submit_by;
    Button post;
    ProgressBar progressBar;
    DatabaseReference notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        initViews();
        post.setOnClickListener(v -> {
            String desc=description.getText().toString();
            String noti_title=title.getText().toString();
            String submit=submit_by.getText().toString();

            if (desc.equals("")) description.setError("Fill the Blank");
            else if(noti_title.equals("")) title.setError("Fill the blank");
            //else if(noti_title.length()<=50) title.setError("Describe in minimum 50 characters");
            else if(submit.equals("")) submit_by.setError("Fill the blank");
            else
                addNotification(desc,noti_title,submit);
        });
    }

    private void addNotification(String desc,String noti_title,String submit_by)
    {
        progressBar.setVisibility(View.VISIBLE);
        post.setVisibility(View.INVISIBLE);
        String uniques_id=notification.push().getKey();
        assert uniques_id != null;
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("title",noti_title);
        hashMap.put("description",desc);
        hashMap.put("date",formatter.format(date));
        hashMap.put("submitby",submit_by);
        notification.child(uniques_id).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Add_Notification_Activity.this, "Successfully created..", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Add_Notification_Activity.this, MainActivity.class));
            }
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.INVISIBLE);
            post.setVisibility(View.VISIBLE);
            Toast.makeText(Add_Notification_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void initViews()
    {
        description=findViewById(R.id.notification_Description);
        title=findViewById(R.id.notification_title);
        submit_by=findViewById(R.id.notification_submit_by);
        post=findViewById(R.id.post_notification);
        progressBar=findViewById(R.id.progressBar);
        notification= FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentNotification");
    }
}