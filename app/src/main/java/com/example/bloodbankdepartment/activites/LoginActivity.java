package com.example.bloodbankdepartment.activites;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbankdepartment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class LoginActivity extends AppCompatActivity
{
    Button btn1,btn2;
    TextView recaptcha;
    FirebaseAuth firebaseAuth;
    EditText email,password,editText_recaptcha;
    Random random;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        alert();
        btn1.setOnClickListener(v -> {
            String email_edittext,password_edittext,recaptcha_edittext;
            String random_number;
            random_number=recaptcha.getText().toString();
            email_edittext=email.getText().toString();
            password_edittext=password.getText().toString();
            recaptcha_edittext=editText_recaptcha.getText().toString();
            if(email_edittext.equals(""))
                email.setError("Fill the blank!");
            else if(password_edittext.equals(""))
                password.setError("Fill the blank");
            else if(recaptcha_edittext.equals(""))
                editText_recaptcha.setError("Fill the blank");
            else if(!recaptcha_edittext.equals(random_number))
                editText_recaptcha.setError("Please enter correct recaptcha!");
            else{
                loginProcess(email_edittext,password_edittext);
            }
        });
        btn2.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgetPassword.class)));
    }

    private void loginProcess(String email_edittext, String password_edittext)
    {
        progressBar.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.INVISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email_edittext,password_edittext)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        progressBar.setVisibility(View.INVISIBLE);
                        btn1.setVisibility(View.VISIBLE);
                        if(firebaseAuth.getCurrentUser().isEmailVerified())
                        {       // Check the employee is exits orr not
                            checkEmployeeId();
                            }
                        else {
                            progressBar.setVisibility(View.INVISIBLE);
                            btn1.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this,"Please verify emailAddress",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                btn1.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView()
    {
        btn1=findViewById(R.id.login_button);
        btn2=findViewById(R.id.forget_password);
        recaptcha=findViewById(R.id.recaptcha);
        email=findViewById(R.id.Login_edittext1);
        password=findViewById(R.id.Login_edittext2);
        editText_recaptcha=findViewById(R.id.edittext_recaptcha);
        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressbar_signIn);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("BloodBankDepartmentInfo");
        random=new Random();
        int recaptcha_num=random.nextInt(2500)+5000;
        String recaptcha_number=String.valueOf(recaptcha_num);
        recaptcha.setText(recaptcha_number);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Verifying...");
    }
    private void alert()
    {
        AlertDialog.Builder alert=new AlertDialog.Builder(LoginActivity.this);
        alert.setMessage("If you are new user then first verify your email(Check your email)")
                .setPositiveButton("Okay", (dialog, which) ->
                        Toast.makeText(LoginActivity.this,"Thank you",Toast.LENGTH_SHORT).show());
        AlertDialog alertDialog=alert.create();
        alertDialog.setTitle("Notice");
        alertDialog.show();
    }

    private void checkEmployeeId(){
        AlertDialog.Builder alertBuilder=new AlertDialog.Builder(this);
        alertBuilder.setTitle("Verify your EmployeeId");
        @SuppressLint("InflateParams")
        final View view= getLayoutInflater().inflate(R.layout.employee_id_view,null);
        alertBuilder.setView(view);
        alertBuilder.setPositiveButton("Verify", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText employeeID=view.findViewById(R.id.employee_id);
                String employeeId=employeeID.getText().toString();
                if(employeeId.equals("")) employeeID.setError("Enter your EmployeeId");
                progressDialog.show();
                String currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String id=snapshot.child("bloodbankid").getValue().toString();
                            if(employeeId.equals(id)){
                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                progressDialog.dismiss();
                            }else
                                Toast.makeText(LoginActivity.this, "Invalid User!", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).setNegativeButton("Cancel",null);
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }
}