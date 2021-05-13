package com.chat.letschat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chat.letschat.Models.User;
import com.chat.letschat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class signUpActivity extends AppCompatActivity {
    ActivitySignUpBinding binding ;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(signUpActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your Account");
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.etemail.getText().toString().isEmpty()) {
                    binding.etemail.setError("Enter your Email");
                    return;
                }if(binding.etpassword.getText().toString().isEmpty()) {
                    binding.etpassword.setError("Enter your Password");
                    return;
                }if(binding.etuserName.getText().toString().isEmpty()) {
                    binding.etuserName.setError("Enter your UserName");
                    return;
                }
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.etemail.getText().toString() , binding.etpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(signUpActivity.this , MainActivity.class);
                            startActivity(intent);
                            User user = new User(binding.etuserName.getText().toString(),binding.etemail.getText().toString(),binding.etpassword.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("User").child(id).setValue(user);

                            Toast.makeText(signUpActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(signUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


        binding.tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signUpActivity.this , SignIn.class);
                startActivity(intent);
            }
        });








    }
}