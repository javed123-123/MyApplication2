package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    EditText password,e_mail;
    Button log_in;
    ProgressBar progressbar;
    FirebaseAuth firebaseAuth;
    TextView signup_page,forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        e_mail=findViewById(R.id.e_mail);
        password=findViewById(R.id.password);
        log_in=findViewById(R.id.log_in);
        progressbar=findViewById(R.id.progress_bar);
        firebaseAuth=FirebaseAuth.getInstance();
        signup_page=findViewById(R.id.sign_up);
        forgot=findViewById(R.id.forgot);

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=e_mail.getText().toString();
                String Password=password.getText().toString();
                if(TextUtils.isEmpty(email)){
                    e_mail.setError("Email is required");
                    return;
                }if(TextUtils.isEmpty(Password)) {
                    password.setError("Password is required");
                    return;
                }
                //progressbar.setVisibility(View.VISIBLE);
                firebaseAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LogIn.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("user",email);
                            startActivity(intent);
                            //finish();
                        }else{
                            Toast.makeText(LogIn.this,"Error ! "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        signup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText reset = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Passwrd?");
                passwordResetDialog.setMessage(("Enter Your Email To Received Reset Link"));
                passwordResetDialog.setView(reset);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = reset.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LogIn.this, "Reset Link Sent To Your Email.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LogIn.this, "Error ! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });
    }
}